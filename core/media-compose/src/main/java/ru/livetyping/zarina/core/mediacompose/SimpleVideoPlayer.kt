package ru.livetyping.zarina.core.mediacompose

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
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
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.analytics.compose.LocalAppMetrica
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
public fun SimpleVideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    isOnScreen: Boolean = true,
    repeatMode: Int = RepeatMode,
    isVolumeEnabled: Boolean = false,
    contentScale: ContentScale = DefaultContentScale,
    videoScalingMode: Int = VideoScalingMode,
    onReadyToPlay: (() -> Unit)? = null,
    surfaceType: Int = SurfaceType,
    cacheDataSourceFactory: CacheDataSource.Factory? = LocalExoPlayerCacheDataSourceFactoryProvider.current?.provide(),
) {
    val data = remember(url) { Data.Url(url) }

    SimpleVideoPlayerImpl(
        data = data,
        isOnScreen = isOnScreen,
        repeatMode = repeatMode,
        isVolumeEnabled = isVolumeEnabled,
        contentScale = contentScale,
        videoScalingMode = videoScalingMode,
        onReadyToPlay = onReadyToPlay,
        surfaceType = surfaceType,
        cacheDataSourceFactory = cacheDataSourceFactory,
        modifier = modifier,
    )
}

@OptIn(UnstableApi::class)
@Composable
public fun SimpleVideoPlayer(
    resId: Int,
    modifier: Modifier = Modifier,
    isOnScreen: Boolean = true,
    repeatMode: Int = RepeatMode,
    isVolumeEnabled: Boolean = false,
    contentScale: ContentScale = DefaultContentScale,
    videoScalingMode: Int = VideoScalingMode,
    onReadyToPlay: (() -> Unit)? = null,
    surfaceType: Int = SurfaceType,
) {
    val data = remember(resId) { Data.Resource(resId) }

    SimpleVideoPlayerImpl(
        data = data,
        isOnScreen = isOnScreen,
        repeatMode = repeatMode,
        isVolumeEnabled = isVolumeEnabled,
        contentScale = contentScale,
        videoScalingMode = videoScalingMode,
        onReadyToPlay = onReadyToPlay,
        surfaceType = surfaceType,
        cacheDataSourceFactory = null,
        modifier = modifier,
    )
}

@OptIn(UnstableApi::class)
@Composable
private fun SimpleVideoPlayerImpl(
    data: Data,
    isOnScreen: Boolean,
    repeatMode: Int,
    isVolumeEnabled: Boolean,
    contentScale: ContentScale,
    videoScalingMode: Int,
    onReadyToPlay: (() -> Unit)?,
    surfaceType: Int,
    cacheDataSourceFactory: CacheDataSource.Factory?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val appMetrica = LocalAppMetrica.current

    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    // Init and release player
    DisposableEffect(context) {
        val newPlayer = initPlayer(
            context = context,
            repeatMode = repeatMode,
            isVolumeEnabled = isVolumeEnabled,
            videoScalingMode = videoScalingMode,
            onReadyToPlay = onReadyToPlay,
            appMetrica = appMetrica,
        )
        player = newPlayer
        Timber.tag(Tag).v("Player initialized")

        onDispose {
            player?.release()
            player = null
            Timber.tag(Tag).v("Player released")
        }
    }

    // Set media
    DisposableEffect(player, data, cacheDataSourceFactory) {
        player?.playFromData(data, cacheDataSourceFactory)
        Timber.tag(Tag).v("Media set")
        onDispose {}
    }

    // Control playback state
    LifecycleStartEffect(player, isOnScreen) {
        player?.play()
        Timber.tag(Tag).v("Playback started")
        onStopOrDispose {
            player?.pause()
            Timber.tag(Tag).v("Playback paused")
        }
    }

    player?.let {
        VideoPlayer(
            player = it,
            surfaceType = surfaceType,
            contentScale = contentScale,
            modifier = modifier.clipToBounds(),
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
    appMetrica: AppMetrica?,
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
                        appMetrica?.reportError(Tag, null, error)
                    }
                }
                addListener(listener)
            }
        }
}

@OptIn(UnstableApi::class)
private fun ExoPlayer.playFromData(data: Data, cacheDataSourceFactory: CacheDataSource.Factory?) {
    when (data) {
        is Data.Url -> playFromUrl(data.url, cacheDataSourceFactory)
        is Data.Resource -> playFromResources(data.resId)
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

@OptIn(UnstableApi::class)
private fun ExoPlayer.playFromResources(resId: Int) {
    val uri: Uri? = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .path(resId.toString())
        .build()
    if (uri != null) {
        val mediaItem = MediaItem.fromUri(uri)
        setMediaItem(mediaItem)
        prepare()
        play()
    }
}

@Stable
private sealed class Data {
    @Immutable
    data class Url(val url: String) : Data()

    @Immutable
    data class Resource(val resId: Int) : Data()
}

private const val RepeatMode = Player.REPEAT_MODE_ONE
private val DefaultContentScale = ContentScale.None
@SuppressLint("UnsafeOptInUsageError")
private const val VideoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
@SuppressLint("UnsafeOptInUsageError")
private const val SurfaceType = SURFACE_TYPE_TEXTURE_VIEW

private const val Tag = "ExoPlayer"
