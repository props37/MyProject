package ru.zarina.zarina.ui.common.component.base.button

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import ru.zarina.zarina.R

// TODO: [Low] Process so that fast clicks lead to playing a full animation

@Composable
fun BouncingIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = rememberRipple(bounded = false, radius = 24.dp),
    isBouncingEnabled: Boolean = true,
    pressedScale: Float = PressedScale,
    content: @Composable () -> Unit,
) {
    IconButtonCustom(
        onClick = onClick,
        enabled = isEnabled,
        interactionSource = interactionSource,
        indication = indication,
        modifier = modifier,
    ) {
        val scaleAnimatable = remember { Animatable(1f) }
        val isPressed = interactionSource.collectIsPressedAsState()
        LaunchedEffect(scaleAnimatable) {
            val animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = 0.01f,
            )

            snapshotFlow { isPressed.value }.collectLatest { isPressed ->
                val targetValue = if (isPressed) pressedScale else 1f
                scaleAnimatable.animateTo(
                    targetValue = targetValue,
                    animationSpec = animationSpec,
                )
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
    BouncingIconButton(
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
