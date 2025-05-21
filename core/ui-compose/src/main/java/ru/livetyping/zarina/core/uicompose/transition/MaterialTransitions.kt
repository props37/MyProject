package ru.livetyping.zarina.core.uicompose.transition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.ui.unit.Density

public object MaterialTransitions {
    public fun sharedAxisEnter(
        axis: MaterialSharedAxis,
        forward: Boolean,
        density: Density,
    ): EnterTransition {
        return when (axis) {
            MaterialSharedAxis.X -> MaterialSharedAxisTransitions.enterX(forward, density)
            MaterialSharedAxis.Y -> MaterialSharedAxisTransitions.enterY(forward, density)
            MaterialSharedAxis.Z -> MaterialSharedAxisTransitions.enterZ(forward)
        }
    }

    public fun sharedAxisExit(
        axis: MaterialSharedAxis,
        forward: Boolean,
        density: Density,
    ): ExitTransition {
        return when (axis) {
            MaterialSharedAxis.X -> MaterialSharedAxisTransitions.exitX(forward, density)
            MaterialSharedAxis.Y -> MaterialSharedAxisTransitions.exitY(forward, density)
            MaterialSharedAxis.Z -> MaterialSharedAxisTransitions.exitZ(forward)
        }
    }

    public fun fadeThroughEnter(): EnterTransition {
        return MaterialFadeThroughTransitions.enter()
    }

    public fun fadeThroughExit(): ExitTransition {
        return MaterialFadeThroughTransitions.exit()
    }
}
