package ru.livetyping.zarina.core.uikit.toast

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.filter
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastController2
import kotlin.math.roundToInt

// TODO: [Top] Rename after full migration
@Composable
public fun ZarinaToastContainer2(
    controller: ZarinaToastController2,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { DefaultWindowInsets },
) {
    val currentMessage by controller.currentMessage.collectAsStateWithLifecycle()

    val overscrollEffect = rememberOverscrollEffect()

    AnimatedContent(
        targetState = currentMessage,
        transitionSpec = {
            val enter = slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down)
            val exit = slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            (enter togetherWith exit).using(sizeTransform = null)
        },
        contentAlignment = Alignment.TopCenter,
        label = "ZarinaToastContainer",
        modifier = modifier.overscroll(overscrollEffect),
    ) { message ->
        var toastHeightPx by remember { mutableIntStateOf(0) }

        val anchoredDraggableState = rememberSaveable(saver = AnchoredDraggableState.Saver()) {
            AnchoredDraggableState(SwipeableState2.Default)
        }

        DisposableEffect(anchoredDraggableState, toastHeightPx, message) {
            val anchors = DraggableAnchors {
                SwipeableState2.Default at 0f
                if (message?.isRemovable == true) {
                    SwipeableState2.Swiped at (-toastHeightPx).toFloat()
                }
            }
            anchoredDraggableState.updateAnchors(anchors)
            onDispose {}
        }

        LaunchedEffect(controller, anchoredDraggableState) {
            snapshotFlow { anchoredDraggableState.settledValue }
                .filter { it == SwipeableState2.Swiped }
                .collect {
                    controller.cancelCurrentToast()
                }
        }

        if (message != null) {
            ZarinaToast2(
                message = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onSizeChanged { toastHeightPx = it.height }
                    .windowInsetsPadding(windowInsetsProvider())
                    .offset {
                        val yOffset = anchoredDraggableState.offset.takeIf { !it.isNaN() } ?: 0f
                        IntOffset(
                            x = 0,
                            y = yOffset
                                .coerceAtMost(0f)
                                .roundToInt(),
                        )
                    }
                    .anchoredDraggable(
                        state = anchoredDraggableState,
                        orientation = Orientation.Vertical,
                        overscrollEffect = overscrollEffect,
                    ),
            )
        }
    }
}

// TODO: [Top] Rename after full migration
private enum class SwipeableState2 { Default, Swiped }

private val DefaultWindowInsets: WindowInsets
    @Composable
    get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
