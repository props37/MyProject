package ru.zarina.zarina.util.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

// TODO: [Low] Remove SliderColors extensions when Material3 versions will become public

@Stable
internal fun SliderColors.thumbColor(enabled: Boolean): Color =
    if (enabled) thumbColor else disabledThumbColor

@Stable
internal fun SliderColors.trackColor(enabled: Boolean, active: Boolean): Color =
    if (enabled) {
        if (active) activeTrackColor else inactiveTrackColor
    } else {
        if (active) disabledActiveTrackColor else disabledInactiveTrackColor
    }

@Stable
internal fun SliderColors.tickColor(enabled: Boolean, active: Boolean): Color =
    if (enabled) {
        if (active) activeTickColor else inactiveTickColor
    } else {
        if (active) disabledActiveTickColor else disabledInactiveTickColor
    }

@OptIn(ExperimentalMaterial3Api::class)
val RangeSliderState.tickFractions: FloatArray
    get() = stepsToTickFractions(steps)

@OptIn(ExperimentalMaterial3Api::class)
val RangeSliderState.coercedActiveRangeStartAsFraction
    get() = calcFraction(
        valueRange.start,
        valueRange.endInclusive,
        activeRangeStart,
    )

@OptIn(ExperimentalMaterial3Api::class)
val RangeSliderState.coercedActiveRangeEndAsFraction
    get() = calcFraction(
        valueRange.start,
        valueRange.endInclusive,
        activeRangeEnd
    )

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
@Stable
private fun calcFraction(a: Float, b: Float, pos: Float): Float =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

private fun stepsToTickFractions(steps: Int): FloatArray {
    return if (steps == 0) {
        floatArrayOf()
    } else {
        FloatArray(steps + 2) { it.toFloat() / (steps + 1) }
    }
}
