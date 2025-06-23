package ru.livetyping.zarina.core.uicompose.popup

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

@SuppressLint("ComposeModifierMissing")
@Composable
public fun AnimatedPopup(
    isVisible: Boolean,
    onDismissRequest: (() -> Unit)? = null,
    alignment: Alignment = Alignment.TopStart,
    offset: IntOffset = IntOffset.Zero,
    properties: PopupProperties = remember { PopupProperties() },
    content: @Composable AnimatedPopupScope.() -> Unit,
) {
    val transitionState = remember { MutableTransitionState(isVisible) }
    transitionState.targetState = isVisible

    if (transitionState.currentState || transitionState.targetState) {
        Popup(
            alignment = alignment,
            offset = offset,
            onDismissRequest = onDismissRequest,
            properties = properties,
        ) {
            val biasAlignment = alignment as? BiasAlignment
            val transformOrigin = if (biasAlignment != null) {
                TransformOrigin(biasAlignment.horizontalBias, biasAlignment.verticalBias)
            } else {
                TransformOrigin.Center
            }

            PopupLayout(
                transitionState = transitionState,
                transformOrigin = transformOrigin,
                content = content,
            )
        }
    }
}

@Composable
public fun AnimatedPopup(
    isVisible: Boolean,
    popupPositionProvider: PopupPositionProvider,
    onDismissRequest: (() -> Unit)? = null,
    transformOrigin: TransformOrigin = TransformOrigin.Center,
    properties: PopupProperties = remember { PopupProperties() },
    content: @Composable AnimatedPopupScope.() -> Unit,
) {
    val transitionState = remember { MutableTransitionState(isVisible) }
    transitionState.targetState = isVisible

    if (transitionState.currentState || transitionState.targetState) {
        Popup(
            popupPositionProvider = popupPositionProvider,
            onDismissRequest = onDismissRequest,
            properties = properties,
        ) {
            PopupLayout(
                transitionState = transitionState,
                transformOrigin = transformOrigin,
                content = content,
            )
        }
    }
}

@Composable
private fun PopupLayout(
    transitionState: TransitionState<Boolean>,
    transformOrigin: TransformOrigin,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedPopupScope.() -> Unit,
) {
    val transition = rememberTransition(
        transitionState = transitionState,
        label = Label,
    )
    val animatedPopupScope = remember(transition) {
        AnimatedPopupScopeImpl(transition)
    }

    val scale by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(durationMillis = ScaleInTransitionDuration, easing = LinearOutSlowInEasing)
            } else {
                tween(durationMillis = 1, delayMillis = ScaleOutTransitionDuration - 1)
            }
        },
    ) { isVisible ->
        if (isVisible) 1f else ScaleOutTarget
    }

    val alpha by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(durationMillis = AlphaInTransitionDuration)
            } else {
                tween(durationMillis = AlphaOutTransitionDuration)
            }
        },
    ) { isVisible ->
        if (isVisible) 1f else 0f
    }

    Box(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.alpha = alpha
            this.transformOrigin = transformOrigin
        },
    ) {
        animatedPopupScope.content()
    }
}

private const val ScaleOutTarget = 0.8f
private const val ScaleInTransitionDuration = 120
private const val ScaleOutTransitionDuration = 75

private const val AlphaInTransitionDuration = 30
private const val AlphaOutTransitionDuration = 75

private const val Label = "AnimatedPopup"
