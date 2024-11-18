package ru.livetyping.zarina.core.uicompose.screenbrightness

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.core.uicommon.behavior.BehaviorController
import ru.livetyping.zarina.core.uicommon.behavior.NoOpBehaviorController

public typealias ScreenBrightnessBehaviorController = BehaviorController<ScreenBrightnessBehavior>

public val LocalScreenBrightnessBehaviorController: ProvidableCompositionLocal<BehaviorController<ScreenBrightnessBehavior>> =
    staticCompositionLocalOf { NoOpBehaviorController() }
