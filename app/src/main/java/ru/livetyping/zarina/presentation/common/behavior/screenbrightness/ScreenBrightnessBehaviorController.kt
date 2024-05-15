package ru.livetyping.zarina.presentation.common.behavior.screenbrightness

import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.base.behavior.BehaviorController
import ru.livetyping.zarina.base.behavior.NoOpBehaviorController

typealias ScreenBrightnessBehaviorController = BehaviorController<ScreenBrightnessBehavior>

val LocalScreenBrightnessBehaviorController =
    staticCompositionLocalOf<ScreenBrightnessBehaviorController> {
        val defaultBehavior = ScreenBrightnessBehavior(ScreenBrightness.DEFAULT)
        NoOpBehaviorController(defaultBehavior)
    }
