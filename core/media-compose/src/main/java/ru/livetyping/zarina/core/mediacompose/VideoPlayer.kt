package ru.livetyping.zarina.core.mediacompose

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

@OptIn(UnstableApi::class)
@Composable
public fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    isOnScreen: Boolean = true,
    playWhenReady: Boolean = true,
    repeatMode: Int = Player.REPEAT_MODE_ONE,
    isVolumeEnabled: Boolean = false,
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
    useCache: Boolean = true,
    onReadyToPlay: (() -> Unit)? = null,
) {
    val exoPlayer = rememberExoPlayer(
        repeatMode = repeatMode,
        isVolumeEnabled = isVolumeEnabled,
    )

    // Update repeatMode and volume
    DisposableEffect(exoPlayer, repeatMode, isVolumeEnabled) {
        exoPlayer.repeatMode = repeatMode
        exoPlayer.volume = if (isVolumeEnabled) 1f else 0f

        onDispose {}
    }

    // Set onReadyToPlay listener
    DisposableEffect(exoPlayer, onReadyToPlay) {
        val listener: Player.Listener? = onReadyToPlay?.let {
            object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        onReadyToPlay()
                    }
                }
            }
        }
        if (listener != null) {
            exoPlayer.addListener(listener)
        }

        onDispose {
            if (listener != null) {
                exoPlayer.removeListener(listener)
            }
        }
    }

    // Release player
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    // Get cache
    val cacheDataSourceFactory = if (useCache) {
        LocalExoPlayerCacheDataSourceFactoryProvider.current?.provide()
    } else {
        null
    }

    // Set media
    DisposableEffect(exoPlayer, url, cacheDataSourceFactory) {
        val dataSourceFactory = cacheDataSourceFactory ?: run {
            Timber.tag(Tag).w("CacheDataSource.Factory is null. Using fallback DataSource factory instead")
            DefaultHttpDataSource.Factory()
        }
        val mediaItem = MediaItem.fromUri(url)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()

        onDispose {}
    }

    // Control playback state
    LifecycleStartEffect(exoPlayer, isOnScreen, playWhenReady) {
        if (isOnScreen && playWhenReady) {
            exoPlayer.play()
        }
        onStopOrDispose { exoPlayer.pause() }
    }

    VideoPlayer(
        exoPlayer = exoPlayer,
        resizeMode = resizeMode,
        modifier = modifier,
    )
}

@OptIn(UnstableApi::class)
@Composable
public fun VideoPlayer(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
) {
    val coroutineScope = rememberCoroutineScope()
    val playerViewState = remember { mutableStateOf<PlayerView?>(null) }

    LifecycleResumeEffect(Unit) {
        val playerView = playerViewState.value
        Timber.tag(Tag).v("onResume")
        playerView?.onResume()

        onPauseOrDispose {
            Timber.tag(Tag).v("onPauseOrDispose")
            playerView?.onPause()
        }
    }

    // Workaround for PlayerView bug due to which the video sometimes doesn't occupy the whole
    // PlayerView size
    val xOffsetJob = remember { mutableStateOf<Job?>(null) }
    val xOffset = remember { mutableIntStateOf(0) }
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    xOffsetJob.value?.cancel()
                    xOffsetJob.value = coroutineScope.launch {
                        Timber.tag(Tag).v("Adjust offset to fix scaling")
                        xOffset.intValue = 1
                        delay(50.milliseconds)
                        xOffset.intValue = 0
                    }
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {}
    }

    AndroidView(
        factory = { context ->
            Timber.tag(Tag).v("AndroidView factory")
            PlayerView(context)
                .apply {
                    useController = false
                    player = exoPlayer
                    this.resizeMode = resizeMode
                }
                .also { playerViewState.value = it }
        },
        update = { playerView ->
            Timber.tag(Tag).v("AndroidView update")
            playerView.player = exoPlayer
            playerView.resizeMode = resizeMode
        },
        onReset = { playerView ->
            Timber.tag(Tag).v("AndroidView onReset")
            playerView.player = null
        },
        onRelease = { playerView ->
            Timber.tag(Tag).v("AndroidView onRelease")
            playerView.player = null
        },
        modifier = modifier.offset { IntOffset(xOffset.intValue, 0) },
    )
}

private const val Tag = "VideoPlayer"
