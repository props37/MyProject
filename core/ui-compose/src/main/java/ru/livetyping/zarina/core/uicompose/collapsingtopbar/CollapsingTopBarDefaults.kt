package ru.livetyping.zarina.core.uicompose.collapsingtopbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// Source: androidx.compose.material3.TopAppBarDefaults

public object CollapsingTopBarDefaults {
    @Composable
    public fun rememberPinnedScrollBehavior(
        state: CollapsingTopBarState = rememberCollapsingTopBarState(),
        canScroll: () -> Boolean = { true },
    ): CollapsingTopBarScrollBehavior = remember(state, canScroll) {
        PinnedScrollBehavior(state = state, canScroll = canScroll)
    }

    @Composable
    public fun rememberEnterAlwaysScrollBehavior(
        state: CollapsingTopBarState = rememberCollapsingTopBarState(),
        canScroll: () -> Boolean = { true },
        snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
        flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay(),
        scrollBeforeContent: () -> Boolean = { true },
    ): CollapsingTopBarScrollBehavior = remember(
        state,
        canScroll,
        snapAnimationSpec,
        flingAnimationSpec,
        scrollBeforeContent,
    ) {
        EnterAlwaysScrollBehavior(
            state = state,
            snapAnimationSpec = snapAnimationSpec,
            flingAnimationSpec = flingAnimationSpec,
            canScroll = canScroll,
            scrollBeforeContent = scrollBeforeContent,
        )
    }

    @Composable
    public fun rememberExitUntilCollapsedScrollBehavior(
        state: CollapsingTopBarState = rememberCollapsingTopBarState(),
        canScroll: () -> Boolean = { true },
        snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
        flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay(),
    ): CollapsingTopBarScrollBehavior = remember(
        state,
        canScroll,
        snapAnimationSpec,
        flingAnimationSpec,
    ) {
        ExitUntilCollapsedScrollBehavior(
            state = state,
            snapAnimationSpec = snapAnimationSpec,
            flingAnimationSpec = flingAnimationSpec,
            canScroll = canScroll,
        )
    }
}
