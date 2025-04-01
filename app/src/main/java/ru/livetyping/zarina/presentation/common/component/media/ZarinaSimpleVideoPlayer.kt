package ru.livetyping.zarina.presentation.common.component.media

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import io.appmetrica.analytics.AppMetrica
import ru.livetyping.zarina.presentation.common.media.exoplayer.LocalExoPlayerCacheHolder
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
fun ZarinaSimpleVideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    isOnScreen: Boolean = true,
    repeatMode: Int = Player.REPEAT_MODE_ONE,
    isVolumeEnabled: Boolean = false,
    contentScale: ContentScale = ContentScale.None,
    videoScalingMode: Int = C.VIDEO_SCALING_MODE_DEFAULT,
    onReadyToPlay: (() -> Unit)? = null,
    surfaceType: Int = SURFACE_TYPE_SURFACE_VIEW,
    cacheDataSourceFactory: CacheDataSource.Factory? = LocalExoPlayerCacheHolder.current?.cacheDataSourceFactory,
) {
    val context = LocalContext.current

    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    LifecycleStartEffect(context) {
        val newPlayer = initPlayer(
            context = context,
            repeatMode = repeatMode,
            isVolumeEnabled = isVolumeEnabled,
            videoScalingMode = videoScalingMode,
            onReadyToPlay = onReadyToPlay,
        )
        player = newPlayer

        onStopOrDispose {
            player?.release()
            player = null
        }
    }

    LifecycleStartEffect(player, isOnScreen, url, cacheDataSourceFactory) {
        if (isOnScreen) player?.playFromUrl(url, cacheDataSourceFactory)

        onStopOrDispose {
            player?.pause()
        }
    }

    player?.let {
        ZarinaVideoPlayer(
            player = it,
            surfaceType = surfaceType,
            contentScale = contentScale,
            modifier = modifier,
        )
    }
}

@OptIn(UnstableApi::class)
private fun initPlayer(
    context: Context,
    repeatMode: Int,
    isVolumeEnabled: Boolean,
    videoScalingMode: Int,
    onReadyToPlay: (() -> Unit)?,
): ExoPlayer {
    return ExoPlayer.Builder(context)
        .setName(Tag)
        .setVideoScalingMode(videoScalingMode)
        .build()
        .apply {
            this.repeatMode = repeatMode
            volume = if (isVolumeEnabled) 1f else 0f
            if (onReadyToPlay != null) {
                val listener = object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) {
                            onReadyToPlay()
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        Timber.tag(Tag).e(error)
                        AppMetrica.reportError(Tag, null, error)
                    }
                }
                addListener(listener)
            }
        }
}

@OptIn(UnstableApi::class)
private fun ExoPlayer.playFromUrl(url: String, cacheDataSourceFactory: CacheDataSource.Factory?) {
    val dataSourceFactory = cacheDataSourceFactory ?: run {
        Timber.tag(Tag).w("CacheDataSource factory is null. Use fallback DataSource factory instead")
        DefaultHttpDataSource.Factory()
    }
    val mediaItem = MediaItem.fromUri(url)
    val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
        .createMediaSource(mediaItem)
    setMediaSource(mediaSource)
    prepare()
    play()
}

private const val Tag = "ZarinaSimpleVideoPlayer"
