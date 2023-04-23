package ru.zarina.zarina.ui.common.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import ru.zarina.zarina.domain.Media
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
    media: List<Media>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        pageCount = media.size,
        modifier = modifier
    ) { pageIndex ->
        val item = media[pageIndex]
        val itemModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
        when (item.type) {
            Media.Type.IMAGE -> ImageItem(
                media = item,
                modifier = itemModifier,
            )

            Media.Type.VIDEO -> VideoItem(
                media = item,
                modifier = itemModifier,
            )
        }
    }
}

@Composable
private fun ImageItem(
    media: Media,
    modifier: Modifier = Modifier,
) {
    // TODO add loader
    AsyncImage(
        model = media.url.value,
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
        modifier = modifier
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoItem(
    media: Media,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val player = remember(context) {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val dataSourceFactory = DefaultDataSource.Factory(context)
                val source = ProgressiveMediaSource
                    .Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(media.url.value))
                setMediaSource(source)
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
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
        modifier = modifier
    )
}
