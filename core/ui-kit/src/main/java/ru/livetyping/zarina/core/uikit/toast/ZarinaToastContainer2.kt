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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import kotlinx.coroutines.flow.filter
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastController2
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import kotlin.math.roundToInt

// TODO: [Top] Rename after full migration
@Composable
public fun ZarinaToastContainer2(
    controller: ZarinaToastController2,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { DefaultWindowInsets },
) {
    val currentMessage by controller.currentMessage.collectAsStateWithLifecycle()

    val overscrollEffect = rememberOverscrollEffect()

    val hazeBackgroundColor = UiKitTheme2.colors.mainBlack
    val hazeTint = rememberHazeTint(hazeBackgroundColor, hazeState.blurEnabled)

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

        val verticalAnchoredDraggableState = rememberSaveable(saver = AnchoredDraggableState.Saver()) {
            AnchoredDraggableState(VerticalSwipeableState.Default)
        }
        val horizontalAnchoredDraggableState = rememberSaveable(saver = AnchoredDraggableState.Saver()) {
            AnchoredDraggableState(HorizontalSwipeableState.Default).apply {
                val anchors = DraggableAnchors { HorizontalSwipeableState.Default at 0f }
                updateAnchors(anchors)
            }
        }

        DisposableEffect(verticalAnchoredDraggableState, toastHeightPx, message) {
            val anchors = DraggableAnchors {
                VerticalSwipeableState.Default at 0f
                if (message?.isRemovable == true) {
                    VerticalSwipeableState.Swiped at (-toastHeightPx).toFloat()
                }
            }
            verticalAnchoredDraggableState.updateAnchors(anchors)
            onDispose {}
        }

        LaunchedEffect(controller, verticalAnchoredDraggableState) {
            snapshotFlow { verticalAnchoredDraggableState.settledValue }
                .filter { it == VerticalSwipeableState.Swiped }
                .collect {
                    controller.cancelCurrentToast()
                }
        }

        if (message != null) {
            ZarinaToast2(
                message = message,
                backgroundColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onSizeChanged { toastHeightPx = it.height }
                    .windowInsetsPadding(windowInsetsProvider())
                    .offset {
                        val yOffset = verticalAnchoredDraggableState.offset.takeIf { !it.isNaN() } ?: 0f
                        IntOffset(x = 0, y = yOffset.roundToInt())
                    }
                    .hazeEffect(
                        state = hazeState,
                        style = HazeStyle(
                            backgroundColor = hazeBackgroundColor,
                            blurRadius = 10.dp,
                            tint = hazeTint,
                            noiseFactor = 0f,
                        ),
                    )
                    .anchoredDraggable(
                        state = verticalAnchoredDraggableState,
                        orientation = Orientation.Vertical,
                        overscrollEffect = overscrollEffect,
                    )
                    .anchoredDraggable(
                        state = horizontalAnchoredDraggableState,
                        orientation = Orientation.Horizontal,
                        overscrollEffect = overscrollEffect,
                    ),
            )
        }
    }
}

@Composable
private fun rememberHazeTint(backgroundColor: Color, isBlurEnabled: Boolean): HazeTint {
    return remember(backgroundColor, isBlurEnabled) {
        val color = if (isBlurEnabled) {
            backgroundColor.copy(alpha = BackgroundAlphaWithBlur)
        } else {
            backgroundColor.copy(alpha = BackgroundAlphaWithoutBlur)
        }
        HazeTint(color)
    }
}

private enum class VerticalSwipeableState { Default, Swiped }

private enum class HorizontalSwipeableState { Default }

private val DefaultWindowInsets: WindowInsets
    @Composable
    get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)

private const val BackgroundAlphaWithBlur = 0.9f
private const val BackgroundAlphaWithoutBlur = 0.95f
