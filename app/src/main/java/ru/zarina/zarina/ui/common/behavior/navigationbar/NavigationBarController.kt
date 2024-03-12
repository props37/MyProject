package ru.zarina.zarina.ui.common.behavior.navigationbar

import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.base.behavior.BehaviorController
import ru.zarina.zarina.base.behavior.NoOpBehaviorController

typealias NavigationBarController = BehaviorController<NavigationBarBehavior>

val LocalNavigationBarController = staticCompositionLocalOf<NavigationBarController> {
    NoOpBehaviorController(NavigationBarBehavior.DEFAULT)
}
