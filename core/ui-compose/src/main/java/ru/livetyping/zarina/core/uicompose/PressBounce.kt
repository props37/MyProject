package ru.livetyping.zarina.core.uicompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
public fun Modifier.pressBounce(
    interactionSource: InteractionSource,
    isEnabled: Boolean = true,
): Modifier {
    val scaleJob = remember { mutableStateOf<Job?>(null) }
    val scaleAnimatable = remember { Animatable(1f) }
    val scaleAnimationSpec = remember {
        spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = VisibilityThreshold,
        )
    }

    LaunchedEffect(scaleAnimatable, interactionSource) {
        interactionSource.interactions.collect {
            when (it) {
                is PressInteraction.Press -> {
                    scaleJob.value?.cancel()
                    scaleJob.value = launch {
                        scaleAnimatable.animateTo(ScalePressed, scaleAnimationSpec)
                    }
                }

                is PressInteraction.Release -> {
                    scaleJob.value?.join()
                    scaleJob.value = launch {
                        scaleAnimatable.animateTo(1f, scaleAnimationSpec)
                    }
                }

                is PressInteraction.Cancel -> {
                    scaleJob.value?.cancel()
                    scaleJob.value = launch {
                        scaleAnimatable.animateTo(1f, scaleAnimationSpec)
                    }
                }
            }
        }
    }

    return this.graphicsLayer {
        if (isEnabled) {
            scaleX = scaleAnimatable.value
            scaleY = scaleAnimatable.value
        }
    }
}

private const val VisibilityThreshold = 0.01f
private const val ScalePressed = 0.8f
