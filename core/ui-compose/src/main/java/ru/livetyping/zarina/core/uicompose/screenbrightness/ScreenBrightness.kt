package ru.livetyping.zarina.core.uicompose.screenbrightness

import android.view.WindowManager

public enum class ScreenBrightness { DEFAULT, MAX, MIN }

public fun ScreenBrightness.toWindowManagerBrightness(): Float = when (this) {
    ScreenBrightness.DEFAULT -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    ScreenBrightness.MAX -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
    ScreenBrightness.MIN -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF
}
