package ru.livetyping.zarina.presentation.common.behavior.bottomnavbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.base.behavior.BehaviorController
import ru.livetyping.zarina.base.behavior.DefaultBehaviorController
import ru.livetyping.zarina.base.behavior.NoOpBehaviorController

typealias BottomNavBarBehaviorController = BehaviorController<BottomNavBarBehavior>

val LocalBottomNavBarBehaviorController = staticCompositionLocalOf<BottomNavBarBehaviorController> {
    NoOpBehaviorController(DefaultBehavior)
}

@Composable
fun rememberBottomNavBarBehaviorController(
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

private val DefaultBehavior: BottomNavBarBehavior
    get() = BottomNavBarBehavior.Hidden(isAnimated = false)
