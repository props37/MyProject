package ru.livetyping.zarina.ui.common.component.slider

import androidx.annotation.IntRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.coercedActiveRangeEndAsFraction
import ru.livetyping.zarina.util.compose.coercedActiveRangeStartAsFraction
import ru.livetyping.zarina.util.compose.thumbColor
import ru.livetyping.zarina.util.compose.tickColor
import ru.livetyping.zarina.util.compose.tickFractions
import ru.livetyping.zarina.util.compose.trackColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarinaRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChanged: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = ZarinaRangeSliderDefaults.colors(),
    startInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    endInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    startThumb: @Composable (RangeSliderState) -> Unit = {
        ZarinaRangeSliderDefaults.Thumb(
            interactionSource = startInteractionSource,
            colors = colors,
            isEnabled = isEnabled,
        )
    },
    endThumb: @Composable (RangeSliderState) -> Unit = {
        ZarinaRangeSliderDefaults.Thumb(
            interactionSource = endInteractionSource,
            colors = colors,
            isEnabled = isEnabled,
        )
    },
    track: @Composable (RangeSliderState) -> Unit = {
        ZarinaRangeSliderDefaults.Track(
            rangeSliderState = it,
            colors = colors,
            isEnabled = isEnabled,
        )
    },
    @IntRange(from = 0)
    steps: Int = 0,
) {
    RangeSlider(
        value = value,
        onValueChange = onValueChanged,
        enabled = isEnabled,
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        startInteractionSource = startInteractionSource,
        endInteractionSource = endInteractionSource,
        modifier = modifier,
        startThumb = startThumb,
        endThumb = endThumb,
        track = track,
        steps = steps,
    )
}

object ZarinaRangeSliderDefaults {
    @Composable
    fun colors(
        thumbColor: Color = UiKitTheme.colors.background.general.regular.default,
        activeTrackColor: Color = UiKitTheme.colors.background.general.inversed.default,
        activeTickColor: Color = Color.Unspecified,
        inactiveTrackColor: Color = UiKitTheme.colors.background.skeleton,
        inactiveTickColor: Color = Color.Unspecified,
        disabledThumbColor: Color = thumbColor,
        disabledActiveTrackColor: Color = activeTrackColor,
        disabledActiveTickColor: Color = Color.Unspecified,
        disabledInactiveTrackColor: Color = inactiveTrackColor,
        disabledInactiveTickColor: Color = Color.Unspecified,
    ): SliderColors = SliderDefaults.colors(
        thumbColor = thumbColor,
        activeTrackColor = activeTrackColor,
        activeTickColor = activeTickColor,
        inactiveTrackColor = inactiveTrackColor,
        inactiveTickColor = inactiveTickColor,
        disabledThumbColor = disabledThumbColor,
        disabledActiveTrackColor = disabledActiveTrackColor,
        disabledActiveTickColor = disabledActiveTickColor,
        disabledInactiveTrackColor = disabledInactiveTrackColor,
        disabledInactiveTickColor = disabledInactiveTickColor,
    )

    @Composable
    fun Thumb(
        interactionSource: MutableInteractionSource,
        modifier: Modifier = Modifier,
        colors: SliderColors = colors(),
        isEnabled: Boolean = true,
        thumbSize: Dp = 20.dp,
    ) {
        val interactions = remember { mutableStateListOf<Interaction>() }
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> interactions.add(interaction)
                    is PressInteraction.Release -> interactions.remove(interaction.press)
                    is PressInteraction.Cancel -> interactions.remove(interaction.press)
                    is DragInteraction.Start -> interactions.add(interaction)
                    is DragInteraction.Stop -> interactions.remove(interaction.start)
                    is DragInteraction.Cancel -> interactions.remove(interaction.start)
                }
            }
        }

        val animationSpec = remember { tween<Dp>(durationMillis = 100) }
        val elevation by animateDpAsState(
            targetValue = if (interactions.isNotEmpty()) 6.dp else 2.dp,
            animationSpec = animationSpec,
            label = "Thumb elevation",
        )
        val shape = CircleShape

        Box(
            modifier = modifier
                .size(thumbSize)
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false, radius = thumbSize),
                )
                .hoverable(
                    interactionSource = interactionSource,
                    enabled = isEnabled,
                )
                .shadow(
                    elevation = if (isEnabled) elevation else 0.dp,
                    shape = shape,
                    clip = false,
                )
                .background(
                    color = colors.thumbColor(isEnabled),
                    shape = shape,
                )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Track(
        rangeSliderState: RangeSliderState,
        modifier: Modifier = Modifier,
        colors: SliderColors = colors(),
        isEnabled: Boolean = true,
        trackHeight: Dp = 1.dp,
    ) {
        val inactiveTrackColor = colors.trackColor(isEnabled, active = false)
        val activeTrackColor = colors.trackColor(isEnabled, active = true)
        val inactiveTickColor = colors.tickColor(isEnabled, active = false)
        val activeTickColor = colors.tickColor(isEnabled, active = true)

        Canvas(
            modifier
                .fillMaxWidth()
                .height(trackHeight),
        ) {
            drawTrack(
                tickFractions = rangeSliderState.tickFractions,
                activeRangeStart = rangeSliderState.coercedActiveRangeStartAsFraction,
                activeRangeEnd = rangeSliderState.coercedActiveRangeEndAsFraction,
                inactiveTrackColor = inactiveTrackColor,
                activeTrackColor = activeTrackColor,
                inactiveTickColor = inactiveTickColor,
                activeTickColor = activeTickColor,
                trackHeight = trackHeight,
            )
        }
    }

    private fun DrawScope.drawTrack(
        tickFractions: FloatArray,
        activeRangeStart: Float,
        activeRangeEnd: Float,
        inactiveTrackColor: Color,
        activeTrackColor: Color,
        inactiveTickColor: Color,
        activeTickColor: Color,
        trackHeight: Dp,
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight
        val tickSize = TickSize.toPx()
        val trackStrokeWidth = trackHeight.toPx()

        drawLine(
            color = inactiveTrackColor,
            start = sliderStart,
            end = sliderEnd,
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round
        )
        val sliderValueEnd = Offset(
            x = sliderStart.x + (sliderEnd.x - sliderStart.x) * activeRangeEnd,
            y = center.y,
        )
        val sliderValueStart = Offset(
            x = sliderStart.x + (sliderEnd.x - sliderStart.x) * activeRangeStart,
            y = center.y,
        )

        drawLine(
            color = activeTrackColor,
            start = sliderValueStart,
            end = sliderValueEnd,
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round,
        )

        for (tick in tickFractions) {
            val outsideFraction = tick > activeRangeEnd || tick < activeRangeStart
            drawCircle(
                color = if (outsideFraction) inactiveTickColor else activeTickColor,
                center = Offset(lerp(sliderStart, sliderEnd, tick).x, center.y),
                radius = tickSize / 2f,
            )
        }
    }

    private val TickSize = 2.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            var value by remember { mutableStateOf(100f..500f) }

            ZarinaRangeSlider(
                value = value,
                onValueChanged = { value = it },
                valueRange = 0f..1000f,
            )
        }
    }
}
