package ru.livetyping.zarina.ui.common.animations

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes

val shake: AnimationSpec<Float> = keyframes {
    durationMillis = 600
    val easing = FastOutLinearInEasing

    for (i in 1..8) {
        val x = when (i % 3) {
            0 -> 8f
            1 -> -8f
            else -> 0f
        }
        x at durationMillis / 10 * i with easing
    }
}
