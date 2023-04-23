package ru.zarina.zarina.ui.common.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.ui.theme.UiKitTheme
import timber.log.Timber

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    media: Media,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
) {
    val isLoaded = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val player = remember(context, cache) {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val cacheValue = cache.value
                val factory = if (cacheValue != null) {
                    CacheDataSource.Factory()
                        .setCache(cacheValue)
                        .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
                } else {
                    DefaultDataSource.Factory(context)
                }
                val source = ProgressiveMediaSource.Factory(factory)
                    .createMediaSource(MediaItem.fromUri(media.url.value))
                setMediaSource(source)
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) isLoaded.value = true
                    }
                })
                prepare()
            }
    }
    DisposableEffect(player) { onDispose { player.release() } }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    Timber.v("onResume, resuming playback")
                    player.play()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Timber.v("onResume, pausing playback")
                    player.pause()
                }

                else -> {}
            }
        }
        lifecycleOwner.value.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.value.lifecycle.removeObserver(observer)
        }
    }
    val playerAlpha by animateFloatAsState(
        targetValue = if (isLoaded.value) 1f else 0f,
        label = "player alpha"
    )
    Box(modifier = modifier) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    hideController()
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    this.player = player
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .graphicsLayer { alpha = playerAlpha }
        )
        CircularProgressIndicator(
            color = UiKitTheme.colors.primaryContentColor,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer { alpha = 1 - playerAlpha }
        )
    }
}
