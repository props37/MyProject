package ru.livetyping.zarina.ui.common.media.exoplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun rememberExoPlayer(
    repeatMode: Int = Player.REPEAT_MODE_ONE,
    isVolumeEnabled: Boolean = false,
    onReadyToPlay: (() -> Unit)? = null,
): ExoPlayer {
    val context = LocalContext.current
    return remember(context) {
        ExoPlayer.Builder(context)
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
                    }
                    addListener(listener)
                }
            }
    }
}
