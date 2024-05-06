package ru.livetyping.zarina.ui.common.behavior.screenbrightness

import android.view.WindowManager

enum class ScreenBrightness { DEFAULT, MAX, MIN }

fun ScreenBrightness.toWindowManagerBrightness(): Float = when (this) {
    ScreenBrightness.DEFAULT -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    ScreenBrightness.MAX -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
    ScreenBrightness.MIN -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF
}
