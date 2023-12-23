package ru.zarina.zarina.ui.common.behavior.bottomnavbar

import androidx.compose.runtime.staticCompositionLocalOf
import ru.zarina.zarina.ui.common.base.behavior.BehaviorController
import ru.zarina.zarina.ui.common.base.behavior.NoopBehaviorController

typealias BottomNavBarBehaviorController = BehaviorController<BottomNavBarBehavior>

val LocalBottomNavBarBehaviorController =
    staticCompositionLocalOf<BottomNavBarBehaviorController> {
        val defaultBehavior = BottomNavBarBehavior.Hidden(isAnimated = false)
        NoopBehaviorController(defaultBehavior)
    }
