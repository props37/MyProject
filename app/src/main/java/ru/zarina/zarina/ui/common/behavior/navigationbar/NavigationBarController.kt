package ru.zarina.zarina.ui.common.behavior.navigationbar

import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.ui.common.behavior.base.BehaviorController
import ru.zarina.zarina.ui.common.behavior.base.NoOpBehaviorController

typealias NavigationBarController = BehaviorController<NavigationBarBehavior>

val LocalNavigationBarController = staticCompositionLocalOf<NavigationBarController> {
    NoOpBehaviorController(NavigationBarBehavior.DEFAULT)
}
