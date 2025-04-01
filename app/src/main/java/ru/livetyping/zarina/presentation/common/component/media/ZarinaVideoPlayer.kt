package ru.livetyping.zarina.presentation.common.component.media

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState

@OptIn(UnstableApi::class)
@Composable
fun ZarinaVideoPlayer(
    player: Player,
    modifier: Modifier = Modifier,
    surfaceType: Int = SURFACE_TYPE_SURFACE_VIEW,
    contentScale: ContentScale = ContentScale.None,
) {
    val presentationState = rememberPresentationState(player)

    PlayerSurface(
        player = player,
        surfaceType = surfaceType,
        modifier = modifier.resizeWithContentScale(
            contentScale = contentScale,
            sourceSizeDp = presentationState.videoSizeDp,
        ),
    )
}
