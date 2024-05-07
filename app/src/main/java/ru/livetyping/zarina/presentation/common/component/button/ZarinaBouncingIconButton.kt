package ru.livetyping.zarina.presentation.common.component.button

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaBouncingIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    loaderSize: Dp = 24.dp,
    loaderColor: Color = UiKitTheme.colors.icon.regular.default,
    isBouncingEnabled: Boolean = true,
    pressedScale: Float = PressedScale,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = rememberRipple(bounded = false, radius = 24.dp),
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scaleAnimatable = remember { Animatable(1f) }
    val scaleAnimationSpec = remember {
        spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = VisibilityThreshold,
        )
    }
    val isScaleAnimationInterruptible = remember { mutableStateOf(true) }

    ZarinaIconButton(
        onClick = {
            onClick()
            coroutineScope.launch {
                isScaleAnimationInterruptible.value = false
                scaleAnimatable.animateTo(pressedScale, scaleAnimationSpec)
                isScaleAnimationInterruptible.value = true
                scaleAnimatable.animateTo(1f, scaleAnimationSpec)
            }
        },
        isEnabled = isEnabled,
        isLoading = isLoading,
        loaderSize = loaderSize,
        loaderColor = loaderColor,
        interactionSource = interactionSource,
        indication = indication,
        modifier = modifier,
    ) {
        val isPressed = interactionSource.collectIsPressedAsState()
        LaunchedEffect(scaleAnimatable) {
            snapshotFlow { isPressed.value }.collectLatest { isPressed ->
                if (isScaleAnimationInterruptible.value) {
                    val targetValue = if (isPressed) pressedScale else 1f
                    scaleAnimatable.animateTo(
                        targetValue = targetValue,
                        animationSpec = scaleAnimationSpec,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .graphicsLayer {
                    if (isBouncingEnabled) {
                        scaleX = scaleAnimatable.value
                        scaleY = scaleAnimatable.value
                    }
                },
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaBouncingIconButton(
        onClick = {},
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_heart_outline_24),
            contentDescription = null,
        )
    }
}

private const val PressedScale = 0.8f
private const val VisibilityThreshold = 0.01f
