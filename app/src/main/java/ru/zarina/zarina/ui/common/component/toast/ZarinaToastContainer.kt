package ru.zarina.zarina.ui.common.component.toast

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.zarina.zarina.ui.common.behavior.systembars.ForcedSystemBarsBehavior
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaMessage
import ru.zarina.zarina.ui.common.zarinatoast.controller.ZarinaToastController
import ru.zarina.zarina.util.compose.rememberAnchoredDraggableState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZarinaToastContainer(
    controller: ZarinaToastController,
    modifier: Modifier = Modifier,
    shouldPaintStatusBar: Boolean = false,
) {
    val currentMessage by controller.currentMessage.collectAsStateWithLifecycle()

    if (shouldPaintStatusBar) {
        currentMessage?.let { message ->
            val isStatusBarContentLight = message.style == ZarinaMessage.Style.DEFAULT
            ForcedSystemBarsBehavior(isStatusBarContentLight = isStatusBarContentLight)
        }
    }

    AnimatedContent(
        targetState = currentMessage,
        transitionSpec = {
            val enter = slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down)
            val exit = slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            (enter togetherWith exit).using(sizeTransform = null)
        },
        label = "ZarinaToastContainer",
        modifier = modifier,
    ) { message ->
        var toastHeightPx by remember { mutableIntStateOf(0) }

        val anchoredDraggableState = rememberAnchoredDraggableState(
            initialValue = SwipeableState.Default,
            confirmValueChange = { message?.isRemovable == true },
        )

        DisposableEffect(anchoredDraggableState, toastHeightPx) {
            val anchors = DraggableAnchors {
                SwipeableState.Default at 0f
                SwipeableState.Swiped at (-toastHeightPx).toFloat()
            }
            anchoredDraggableState.updateAnchors(anchors)
            onDispose {}
        }

        LaunchedEffect(controller, anchoredDraggableState) {
            snapshotFlow { anchoredDraggableState.progress }
                .collect { progress ->
                    if (progress == 1f && anchoredDraggableState.targetValue == SwipeableState.Swiped) {
                        controller.hideCurrentToast()
                    }
                }
        }

        if (message != null) {
            ZarinaToast(
                message = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .anchoredDraggable(
                        state = anchoredDraggableState,
                        orientation = Orientation.Vertical,
                    )
                    .onSizeChanged { toastHeightPx = it.height }
                    .offset {
                        val yOffset = anchoredDraggableState.offset.takeIf { !it.isNaN() } ?: 0f
                        IntOffset(
                            x = 0,
                            y = yOffset
                                .coerceAtMost(0f)
                                .roundToInt(),
                        )
                    },
            )
        }
    }
}

private enum class SwipeableState { Default, Swiped }
