package ru.zarina.zarina.ui.common.behavior.systembars

import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.base.behavior.BehaviorController
import ru.zarina.zarina.base.behavior.NoOpBehaviorController

typealias SystemBarsBehaviorController = BehaviorController<SystemBarsBehavior>

val LocalSystemBarsBehaviorController = staticCompositionLocalOf<SystemBarsBehaviorController> {
    val defaultBehavior = SystemBarsBehavior(
        isStatusBarContentLight = false,
        isNavigationBarContentLight = false,
    )
    NoOpBehaviorController(defaultBehavior)
}
