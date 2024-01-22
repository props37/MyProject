package ru.zarina.zarina.ui.common.behavior.systembars

import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.ui.common.behavior.base.BehaviorController
import ru.zarina.zarina.ui.common.behavior.base.NoOpBehaviorController

typealias SystemBarsBehaviorController = BehaviorController<SystemBarsBehavior>

val LocalSystemBarsBehaviorController = staticCompositionLocalOf<SystemBarsBehaviorController> {
    val defaultBehavior = SystemBarsBehavior(
        isStatusBarContentLight = false,
        isNavigationBarContentLight = false,
    )
    NoOpBehaviorController(defaultBehavior)
}
