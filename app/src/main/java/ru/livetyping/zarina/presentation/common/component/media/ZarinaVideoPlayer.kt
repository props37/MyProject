package ru.livetyping.zarina.presentation.common.component.media

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun ZarinaVideoPlayer(
    player: Player,
    modifier: Modifier = Modifier,
    surfaceType: Int = SURFACE_TYPE_SURFACE_VIEW,
    contentScale: ContentScale = ContentScale.None,
) {
    val coroutineScope = rememberCoroutineScope()

    // Workaround for a bug that causes PlayerSurface to turn white when the app
    // goes to the foreground from the background.
    // Without this workaround PlayerSurface becomes fully functional when the user
    // touches the screen
    var xOffset by remember { mutableIntStateOf(0) }
    LifecycleStartEffect(Unit) {
        coroutineScope.launch {
            xOffset = 1
            delay(50)
            xOffset = 0
        }
        onStopOrDispose {}
    }

    val presentationState = rememberPresentationState(player)

    PlayerSurface(
        player = player,
        surfaceType = surfaceType,
        modifier = modifier
            .resizeWithContentScale(
                contentScale = contentScale,
                sourceSizeDp = presentationState.videoSizeDp,
            )
            .offset { IntOffset(xOffset, 0) },
    )
}
