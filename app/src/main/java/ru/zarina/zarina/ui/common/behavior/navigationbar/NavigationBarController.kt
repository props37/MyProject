package ru.zarina.zarina.ui.common.behavior.navigationbar

import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.ui.common.base.behavior.IBehaviorController
import ru.zarina.zarina.ui.common.base.behavior.NoopBehaviorController

typealias NavigationBarController = IBehaviorController<NavigationBarBehavior>

val LocalNavigationBarController = staticCompositionLocalOf<NavigationBarController> {
    NoopBehaviorController(NavigationBarBehavior.DEFAULT)
}
