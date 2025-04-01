package ru.livetyping.zarina.presentation.common.media.exoplayer

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayer(
    repeatMode: Int = Player.REPEAT_MODE_ONE,
    isVolumeEnabled: Boolean = false,
    videoScalingMode: Int = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING,
    onReadyToPlay: (() -> Unit)? = null,
): ExoPlayer {
    val context = LocalContext.current
    val currentOnReadyToPlay by rememberUpdatedState(onReadyToPlay)

    return remember(context) {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val listener = object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) {
                            currentOnReadyToPlay?.invoke()
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        Timber.tag(TAG).e(error)
                    }
                }
                addListener(listener)
            }
    }.apply {
        this.repeatMode = repeatMode
        volume = if (isVolumeEnabled) 1f else 0f
        this.videoScalingMode = videoScalingMode
    }
}

private const val TAG = "ExoPlayer"
