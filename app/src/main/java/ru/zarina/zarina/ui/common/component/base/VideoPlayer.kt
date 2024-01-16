package ru.zarina.zarina.ui.common.component.base

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_FIT,
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
