package ru.livetyping.zarina.core.uicompose.popup

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable

@Stable
public interface AnimatedPopupScope {
    public val transition: Transition<Boolean>
}

@Stable
internal class AnimatedPopupScopeImpl(
    override val transition: Transition<Boolean>,
) : AnimatedPopupScope
