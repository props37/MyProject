package ru.livetyping.zarina.ui.common.component.snack

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.ui.common.zarinasnack.controller.ZarinaSnackController
import ru.livetyping.zarina.util.compose.rememberAnchoredDraggableState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZarinaSnackContainer(
    controller: ZarinaSnackController,
    modifier: Modifier = Modifier,
) {
    val currentMessage by controller.currentMessage.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = currentMessage,
        transitionSpec = {
            val enter = slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            val exit = slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
            (enter togetherWith exit).using(sizeTransform = null)
        },
        contentAlignment = Alignment.BottomCenter,
        label = "ZarinaSnackContainer",
        modifier = modifier,
    ) { message ->
        var snackHeightPx by remember { mutableIntStateOf(0) }

        val anchoredDraggableState = rememberAnchoredDraggableState(
            initialValue = SwipeableState.Default,
            confirmValueChange = { message?.isRemovable == true },
        )

        DisposableEffect(anchoredDraggableState, snackHeightPx) {
            val anchors = DraggableAnchors {
                SwipeableState.Default at 0f
                SwipeableState.Swiped at snackHeightPx.toFloat()
            }
            anchoredDraggableState.updateAnchors(anchors)
            onDispose {}
        }

        LaunchedEffect(controller, anchoredDraggableState) {
            snapshotFlow { anchoredDraggableState.progress }
                .map { it == 1f }
                .distinctUntilChanged()
                .collect { isAnimationCompleted ->
                    if (
                        isAnimationCompleted
                        && anchoredDraggableState.targetValue == SwipeableState.Swiped
                    ) {
                        controller.hideCurrentSnack()
                    }
                }
        }

        if (message != null) {
            ZarinaSnack(
                message = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { snackHeightPx = it.height }
                    .offset {
                        val yOffset = anchoredDraggableState.offset.takeIf { !it.isNaN() } ?: 0f
                        IntOffset(
                            x = 0,
                            y = yOffset
                                .coerceAtLeast(0f)
                                .roundToInt(),
                        )
                    }
                    .anchoredDraggable(
                        state = anchoredDraggableState,
                        orientation = Orientation.Vertical,
                    ),
            )
        }
    }
}

private enum class SwipeableState { Default, Swiped }
