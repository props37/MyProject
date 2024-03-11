package ru.zarina.zarina.ui.screens.catalog.filters.components.items

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.max
import kotlin.math.roundToInt

@Composable
fun PriceItem(
    selectedMinValue: Int,
    selectedMaxValue: Int,
    minValue: Int,
    maxValue: Int,
    onSelectedValueChange: (min: Int, max: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.price),
                color = UiKitTheme.colorsOld.primaryContentColor,
                style = UiKitTheme.typographyOld.circle1718,
            )
            Text(
                text = stringResource(
                    id = R.string.range,
                    stringResource(id = R.string.currency_amount_rubles, selectedMinValue),
                    stringResource(id = R.string.currency_amount_rubles, selectedMaxValue),
                ),
                color = UiKitTheme.colorsOld.primaryContentColor,
                style = UiKitTheme.typographyOld.circle1718,
            )
        }
        PriceSlider(
            selectedMinValue = selectedMinValue,
            selectedMaxValue = selectedMaxValue,
            minValue = minValue,
            maxValue = maxValue,
            onSelectedValueChange = onSelectedValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    max(
                        WindowInsets.safeGestures
                            .only(WindowInsetsSides.Horizontal)
                            .asPaddingValues(),
                        PaddingValues(horizontal = 16.dp)
                    )
                )
        )
    }
}


@Composable
private fun PriceSlider(
    selectedMinValue: Int,
    selectedMaxValue: Int,
    minValue: Int,
    maxValue: Int,
    onSelectedValueChange: (min: Int, max: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedSelectedMinValue by animateIntAsState(
        targetValue = selectedMinValue,
        label = "animated selected min value",
    )
    val animatedSelectedMaxValue by animateIntAsState(
        targetValue = selectedMaxValue,
        label = "animated selected max value",
    )
    RangeSlider(
        value = animatedSelectedMinValue.toFloat()..animatedSelectedMaxValue.toFloat(),
        valueRange = minValue.toFloat()..maxValue.toFloat(),
        onValueChange = {
            onSelectedValueChange(it.start.roundToInt(), it.endInclusive.roundToInt())
        },
        colors = SliderDefaults.colors(
            thumbColor = UiKitTheme.colorsOld.primaryButtonBackground,
            activeTrackColor = UiKitTheme.colorsOld.primaryButtonBackground,
            activeTickColor = Color.Transparent,
            inactiveTrackColor = UiKitTheme.colorsOld.primaryButtonDisabledBackground,
            inactiveTickColor = Color.Transparent,
            disabledActiveTickColor = Color.Transparent,
            disabledInactiveTickColor = Color.Transparent,
            disabledActiveTrackColor = UiKitTheme.colorsOld.primaryButtonBackground,
            disabledInactiveTrackColor = UiKitTheme.colorsOld.hint,
        ),
        modifier = modifier,
    )
}
