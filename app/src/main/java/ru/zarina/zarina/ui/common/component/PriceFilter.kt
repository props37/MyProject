package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderPositions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.common.primitives.Longs.max
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.filter.PriceFilter
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldSize
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import kotlin.math.min

// TODO: [High] Add visual transformations to text
// TODO: [Low] Adjust slider thumbs appearance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceFilter(
    filter: PriceFilter,
    limits: LongRange,
    onFilterChanged: (PriceFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        var minPrice by remember(filter, limits) { mutableStateOf(filter.min) }
        var maxPrice by remember(filter, limits) { mutableStateOf(filter.max) }

        Text(
            text = stringResource(R.string.price_rubles),
            style = UiKitTheme.typographyReworked.secondary.light,
            color = UiKitTheme.colorsReworked.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = minPrice,
                onValueChanged = { minPrice = it },
                anchorValue = limits.first,
                leadingText = stringResource(R.string.from).lowercase(),
                onClearClicked = {
                    val newMinPrice = null
                    val newMaxPrice = coerceMaxPrice(maxPrice, newMinPrice, limits)
                    minPrice = newMinPrice
                    maxPrice = newMinPrice
                    onFilterChanged(PriceFilter(newMinPrice, newMaxPrice))
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { state ->
                        if (!state.isFocused) {
                            val newMinPrice = coerceMinPrice(minPrice, maxPrice, limits)
                            minPrice = newMinPrice
                            onFilterChanged(PriceFilter(newMinPrice, maxPrice))
                        }
                    },
            )

            TextField(
                value = maxPrice,
                onValueChanged = { maxPrice = it },
                anchorValue = limits.last,
                leadingText = stringResource(R.string.to).lowercase(),
                onClearClicked = {
                    val newMaxPrice = null
                    val newMinPrice = coerceMinPrice(minPrice, newMaxPrice, limits)
                    minPrice = newMinPrice
                    maxPrice = newMinPrice
                    onFilterChanged(PriceFilter(newMinPrice, newMaxPrice))
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { state ->
                        if (!state.isFocused) {
                            val newMaxPrice = coerceMaxPrice(maxPrice, minPrice, limits)
                            maxPrice = newMaxPrice
                            onFilterChanged(PriceFilter(minPrice, newMaxPrice))
                        }
                    },
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: [High] Extract
        RangeSlider(
            value = createSliderValue(minPrice, maxPrice, limits),
            onValueChange = {
                minPrice = it.start.toLong()
                maxPrice = it.endInclusive.toLong()
            },
            valueRange = limits.first.toFloat()..limits.last.toFloat(),
            onValueChangeFinished = {
                val newMinPrice = coerceMinPrice(minPrice, maxPrice, limits)
                val newMaxPrice = coerceMaxPrice(maxPrice, minPrice, limits)
                minPrice = newMinPrice
                maxPrice = newMinPrice
                onFilterChanged(PriceFilter(newMinPrice, newMaxPrice))
            },
            steps = 0,
            colors = SliderDefaults.colors(
                thumbColor = UiKitTheme.colorsReworked.icon.inversed.default,
                activeTrackColor = UiKitTheme.colorsReworked.background.general.inversed.default,
                inactiveTrackColor = UiKitTheme.colorsReworked.background.skeleton,
            ),
            track = { Track(sliderPositions = it) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun TextField(
    value: Long?,
    onValueChanged: (Long?) -> Unit,
    anchorValue: Long,
    leadingText: String,
    onClearClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val valueString = value?.toString().orEmpty()

    ZarinaTextField(
        value = valueString,
        onValueChanged = { string ->
            onValueChanged(string.toLongOrNull())
        },
        size = ZarinaTextFieldSize.Small,
        placeholder = { Text(text = anchorValue.toString()) },
        leadingContent = {
            Text(
                text = leadingText,
                style = UiKitTheme.typographyReworked.secondary.light,
                color = UiKitTheme.colorsReworked.text.general.regular.muted,
                modifier = Modifier.padding(start = 8.dp),
            )
        },
        innerTrailingContent = {
            AnimatedVisibility(
                visible = valueString.isNotEmpty(),
                enter = remember { AnimatedContentDefaultEnterTransition },
                exit = remember { AnimatedContentDefaultExitTransition },
            ) {
                ZarinaTextFieldDefaults.ClearButton(
                    onClick = onClearClicked,
                    iconSize = 16.dp,
                    indication = rememberRipple(bounded = false, radius = 6.dp),
                )
            }
        },
        keyboardOptions = remember {
            KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            )
        },
        singleLine = true,
        modifier = modifier,
    )
}

@Composable
private fun Track(
    sliderPositions: SliderPositions,
    modifier: Modifier = Modifier,
    activeTrackColor: Color = UiKitTheme.colorsReworked.background.general.inversed.default,
    inactiveTrackColor: Color = UiKitTheme.colorsReworked.background.skeleton,
    trackHeight: Dp = 1.dp,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(trackHeight),
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight
        val trackStrokeWidth = trackHeight.toPx()
        drawLine(
            color = inactiveTrackColor,
            start = sliderStart,
            end = sliderEnd,
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Square,
        )

        val sliderValueEnd = Offset(
            x = sliderStart.x +
                    (sliderEnd.x - sliderStart.x) * sliderPositions.activeRange.endInclusive,
            y = center.y
        )
        val sliderValueStart = Offset(
            x = sliderStart.x +
                    (sliderEnd.x - sliderStart.x) * sliderPositions.activeRange.start,
            y = center.y
        )
        drawLine(
            activeTrackColor,
            sliderValueStart,
            sliderValueEnd,
            trackStrokeWidth,
            StrokeCap.Square,
        )
    }
}

private fun coerceMinPrice(minPrice: Long?, maxPrice: Long?, limits: LongRange): Long? {
    val max = maxPrice?.let { minOf(it, limits.last) } ?: limits.last
    return minPrice?.coerceIn(limits.first, max)
}

private fun coerceMaxPrice(maxPrice: Long?, minPrice: Long?, limits: LongRange): Long? {
    val min = minPrice?.let { maxOf(it, limits.first) } ?: limits.first
    return maxPrice?.coerceIn(min, limits.last)
}

private fun createSliderValue(
    minPrice: Long?,
    maxPrice: Long?,
    limits: LongRange,
): ClosedFloatingPointRange<Float> {
    val minValue = minPrice?.coerceIn(
        minimumValue = limits.first,
        maximumValue = max(maxPrice ?: limits.last, limits.last),
    ) ?: limits.first
    val maxValue = maxPrice?.coerceIn(
        minimumValue = min(minPrice ?: limits.first, limits.first),
        maximumValue = limits.last,
    ) ?: limits.last
    return minValue.toFloat()..maxValue.toFloat()
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        var filter by remember { mutableStateOf(PriceFilter.EMPTY) }
        val limits = 799L..17999L

        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            PriceFilter(
                filter = filter,
                limits = limits,
                onFilterChanged = { filter = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
