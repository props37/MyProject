package ru.zarina.zarina.ui.common.behavior.bottomnavbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.ui.common.behavior.base.BehaviorController
import ru.zarina.zarina.ui.common.behavior.base.DefaultBehaviorController
import ru.zarina.zarina.ui.common.behavior.base.NoopBehaviorController

typealias BottomNavBarBehaviorController = BehaviorController<BottomNavBarBehavior>

val LocalBottomNavBarBehaviorController = staticCompositionLocalOf<BottomNavBarBehaviorController> {
    NoopBehaviorController(DefaultBehavior)
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
