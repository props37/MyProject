package ru.livetyping.zarina.presentation.common.component.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.media.exoplayer.LocalExoPlayerCacheHolder
import ru.livetyping.zarina.presentation.common.media.exoplayer.rememberExoPlayer
import timber.log.Timber

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ZarinaVideoPlayer(
    url: Url,
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

    // Set media
    val cacheDataSourceFactory = if (useCache) {
        LocalExoPlayerCacheHolder.current?.cacheDataSourceFactory
    } else {
        null
    }
    DisposableEffect(exoPlayer, url, cacheDataSourceFactory) {
        val dataSourceFactory = cacheDataSourceFactory ?: run {
            Timber.w("CacheDataSource factory is null. Use fallback DataSource factory instead")
            DefaultHttpDataSource.Factory()
        }
        val mediaItem = MediaItem.fromUri(url.value)
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

    ZarinaVideoPlayer(
        exoPlayer = exoPlayer,
        resizeMode = resizeMode,
        modifier = modifier,
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ZarinaVideoPlayer(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
) {
    val playerViewState = remember { mutableStateOf<PlayerView?>(null) }

    LifecycleResumeEffect(Unit) {
        val playerView = playerViewState.value
        Timber.tag(TAG).v("onResume")
        playerView?.onResume()

        onPauseOrDispose {
            Timber.tag(TAG).v("onPauseOrDispose")
            playerView?.onPause()
        }
    }

    AndroidView(
        factory = { context ->
            Timber.tag(TAG).v("AndroidView factory")
            PlayerView(context)
                .apply {
                    useController = false
                    player = exoPlayer
                    this.resizeMode = resizeMode
                }
                .also { playerViewState.value = it }
        },
        update = { playerView ->
            Timber.tag(TAG).v("AndroidView update")
            playerView.player = exoPlayer
            playerView.resizeMode = resizeMode
        },
        onReset = { playerView ->
            Timber.tag(TAG).v("AndroidView onReset")
            playerView.player = null
        },
        onRelease = { playerView ->
            Timber.tag(TAG).v("AndroidView onRelease")
            playerView.player = null
        },
        modifier = modifier,
    )
}

private const val TAG = "VideoPlayer"
