package ru.livetyping.zarina.core.uicompose.systembars

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.core.uicommon.behavior.BehaviorController
import ru.livetyping.zarina.core.uicommon.behavior.NoOpBehaviorController

public typealias SystemBarsBehaviorController = BehaviorController<SystemBarsBehavior>

public val LocalSystemBarsBehaviorController: ProvidableCompositionLocal<BehaviorController<SystemBarsBehavior>> =
    staticCompositionLocalOf { NoOpBehaviorController() }
