package ru.livetyping.zarina.core.ui.compose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// TODO: [Low] Remove when the compose.animation version will have contentKey parameter
@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun <T> Crossfade(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    contentKey: (targetState: T) -> Any? = { it },
    label: String = "Crossfade",
    content: @Composable (T) -> Unit
) {
    val transition = updateTransition(targetState, label)
    transition.Crossfade(
        modifier = modifier,
        animationSpec = animationSpec,
        contentKey = contentKey,
        content = content,
    )
}
