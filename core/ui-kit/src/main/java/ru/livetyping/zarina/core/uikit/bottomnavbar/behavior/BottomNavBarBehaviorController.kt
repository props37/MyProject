package ru.livetyping.zarina.core.uikit.bottomnavbar.behavior

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.core.uicommon.behavior.BehaviorController
import ru.livetyping.zarina.core.uicommon.behavior.DefaultBehaviorController
import ru.livetyping.zarina.core.uicommon.behavior.NoOpBehaviorController

public typealias BottomNavBarBehaviorController = BehaviorController<BottomNavBarBehavior>

@Suppress("ComposeCompositionLocalUsage")
public val LocalBottomNavBarBehaviorController: ProvidableCompositionLocal<BottomNavBarBehaviorController> =
    staticCompositionLocalOf { NoOpBehaviorController() }

@Composable
public fun rememberBottomNavBarBehaviorController(
    defaultBehavior: BottomNavBarBehavior = DefaultBehavior,
): BehaviorController<BottomNavBarBehavior> {
    val controller = remember {
        DefaultBehaviorController(defaultBehavior)
    }
    DisposableEffect(defaultBehavior) {
        controller.setDefaultBehavior(defaultBehavior)
        onDispose {}
    }
    return controller
}

internal val DefaultBehavior: BottomNavBarBehavior
    get() = BottomNavBarBehavior.Hidden(isAnimated = false)
