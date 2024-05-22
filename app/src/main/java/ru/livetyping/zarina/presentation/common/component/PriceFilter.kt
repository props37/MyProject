package ru.livetyping.zarina.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.PriceRange
import ru.livetyping.zarina.domain.filter.PriceFilter
import ru.livetyping.zarina.presentation.common.component.slider.ZarinaRangeSlider
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldSize
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import kotlin.math.max
import kotlin.math.min

// TODO: [Low] Add visual transformations to text. See https://medium.com/@patilshreyas/filtering-and-modifying-text-input-in-jetpack-compose-way-8f7eeedd958
// TODO: [Low] Refactor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PriceFilter(
    filter: PriceFilter,
    onFilterChanged: (PriceFilter) -> Unit,
    modifier: Modifier = Modifier,
    sliderAdditionalHorizontalPadding: Dp = 0.dp,
) {
    var minPrice by remember(filter) { mutableStateOf(filter.min) }
    var maxPrice by remember(filter) { mutableStateOf(filter.max) }

    val limits by rememberUpdatedState(filter.limits)

    val isMinTextFieldFocused = remember { mutableStateOf(false) }
    val isMaxTextFieldFocused = remember { mutableStateOf(false) }

    val updatedMinPrice by rememberUpdatedState(minPrice)
    val updatedMaxPrice by rememberUpdatedState(maxPrice)
    val isImeVisibleState = rememberUpdatedState(WindowInsets.isImeVisible)
    LaunchedEffect(onFilterChanged) {
        snapshotFlow { isImeVisibleState.value }
            .distinctUntilChanged()
            .collect { isImeVisible ->
                val isAnyTextFieldFocused =
                    isMinTextFieldFocused.value || isMaxTextFieldFocused.value
                if (!isImeVisible && isAnyTextFieldFocused) {
                    val newMinPrice = updatedMinPrice?.coerceMinPrice(updatedMaxPrice, limits)
                    val newMaxPrice = updatedMaxPrice?.coerceMaxPrice(newMinPrice, limits)
                    minPrice = newMinPrice
                    maxPrice = newMaxPrice
                    onFilterChanged(filter.copy(min = newMinPrice, max = newMaxPrice))
                }
            }
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.price_rubles),
            style = UiKitTheme.typography.secondary.light,
            color = UiKitTheme.colors.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = minPrice,
                onValueChanged = { minPrice = it },
                placeholderValue = limits.min,
                leadingText = stringResource(R.string.from).lowercase(),
                onClearClicked = {
                    val newMinPrice = null
                    val newMaxPrice = maxPrice?.coerceMaxPrice(newMinPrice, limits)
                    minPrice = newMinPrice
                    maxPrice = newMaxPrice
                    onFilterChanged(filter.copy(min = newMinPrice, max = newMaxPrice))
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { state ->
                        isMinTextFieldFocused.value = state.isFocused
                        if (!state.isFocused) {
                            val newMinPrice = minPrice?.coerceMinPrice(maxPrice, limits)
                            minPrice = newMinPrice
                            onFilterChanged(filter.copy(min = newMinPrice, max = maxPrice))
                        }
                    },
            )

            TextField(
                value = maxPrice,
                onValueChanged = { maxPrice = it },
                placeholderValue = limits.max,
                leadingText = stringResource(R.string.to).lowercase(),
                onClearClicked = {
                    val newMaxPrice = null
                    val newMinPrice = minPrice?.coerceMinPrice(newMaxPrice, limits)
                    minPrice = newMinPrice
                    maxPrice = newMaxPrice
                    onFilterChanged(filter.copy(min = newMinPrice, max = newMaxPrice))
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { state ->
                        isMaxTextFieldFocused.value = state.isFocused
                        if (!state.isFocused) {
                            val newMaxPrice = maxPrice?.coerceMaxPrice(minPrice, limits)
                            maxPrice = newMaxPrice
                            onFilterChanged(filter.copy(min = minPrice, max = newMaxPrice))
                        }
                    },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ZarinaRangeSlider(
            value = createSliderValue(minPrice, maxPrice, limits),
            onValueChanged = {
                val startInt = it.start.toInt()
                val endInt = it.endInclusive.toInt()
                minPrice = if (startInt != limits.min) startInt else null
                maxPrice = if (endInt != limits.max) endInt else null
            },
            valueRange = limits.min.toFloat()..limits.max.toFloat(),
            onValueChangeFinished = {
                val newMinPrice = minPrice?.coerceMinPrice(maxPrice, limits)
                val newMaxPrice = maxPrice?.coerceMaxPrice(minPrice, limits)
                minPrice = newMinPrice
                maxPrice = newMaxPrice
                onFilterChanged(filter.copy(min = newMinPrice, max = newMaxPrice))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sliderAdditionalHorizontalPadding),
        )
    }
}

@Composable
private fun TextField(
    value: Int?,
    onValueChanged: (Int?) -> Unit,
    placeholderValue: Int,
    leadingText: String,
    onClearClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val valueString = value?.toString().orEmpty()

    ZarinaTextField(
        value = valueString,
        onValueChanged = { string ->
            onValueChanged(string.toIntOrNull())
        },
        size = ZarinaTextFieldSize.Small,
        placeholder = {
            Text(text = placeholderValue.toString())
        },
        leadingContent = {
            Text(
                text = leadingText,
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colors.text.general.regular.muted,
                modifier = Modifier.padding(start = 8.dp),
            )
        },
        innerTrailingContent = {
            ZarinaTextFieldDefaults.ClearButton(
                isVisible = valueString.isNotEmpty(),
                onClick = onClearClicked,
                iconSize = 16.dp,
                indication = ripple(bounded = false, radius = 6.dp),
            )
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

private fun Int.coerceMinPrice(maxPrice: Int?, limits: PriceRange): Int {
    val max = maxPrice?.let { minOf(it.coerceAtLeast(limits.min), limits.max) } ?: limits.max
    return this.coerceIn(limits.min, max)
}

private fun Int.coerceMaxPrice(minPrice: Int?, limits: PriceRange): Int {
    val min = minPrice?.let { maxOf(it.coerceAtMost(limits.max), limits.min) } ?: limits.min
    return this.coerceIn(min, limits.max)
}

private fun createSliderValue(
    minPrice: Int?,
    maxPrice: Int?,
    limits: PriceRange,
): ClosedFloatingPointRange<Float> {
    val minValue = minPrice?.coerceIn(
        minimumValue = limits.min,
        maximumValue = max(maxPrice ?: limits.max, limits.max),
    ) ?: limits.min
    val maxValue = maxPrice?.coerceIn(
        minimumValue = min(minPrice ?: limits.min, limits.min),
        maximumValue = limits.max,
    ) ?: limits.max
    return minValue.toFloat()..maxValue.toFloat()
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        var filter by remember {
            mutableStateOf(PriceFilter(min = null, max = null, limits = PriceRange(799, 17999)))
        }

        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            PriceFilter(
                filter = filter,
                onFilterChanged = { filter = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
