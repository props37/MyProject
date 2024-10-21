package ru.livetyping.zarina.core.uicompose.collapsingtopbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.runtime.Stable
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection

// Source: androidx.compose.material3.TopAppBarScrollBehavior

@Stable
public interface CollapsingTopBarScrollBehavior {
    public val state: CollapsingTopBarState
    public val isPinned: Boolean
    public val snapAnimationSpec: AnimationSpec<Float>?
    public val flingAnimationSpec: DecayAnimationSpec<Float>?
    public val nestedScrollConnection: NestedScrollConnection
}
