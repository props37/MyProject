package ru.livetyping.zarina.presentation.common.behavior.screenbrightness

import ru.livetyping.zarina.base.behavior.Behavior

data class ScreenBrightnessBehavior(
    val brightness: ScreenBrightness,
) : Behavior
