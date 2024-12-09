package ru.livetyping.zarina.core.uikit.sizeselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SizeSelectorSizeList(
    sizes: List<SizeSelectorSizeItem>,
    onSizeClicked: (SizeSelectorSizeItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        sizes.forEachIndexed { index, size ->
            key(size.id) {
                Size(
                    size = size,
                    onClick = { onSizeClicked(size) },
                )

                if (index < sizes.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Size(
    size: SizeSelectorSizeItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = onClick,
        startContent = {
            val textColor = if (size.isAvailable) {
                UiKitTheme.colors.text.general.regular.default
            } else {
                UiKitTheme.colors.text.general.regular.disabled
            }

            Text(
                text = size.size.capitalize(Locale.current),
                style = SizeSelectorDefaults.SizeMainTextStyle,
                color = textColor,
            )
        },
        endContent = {
            val availableHeights = remember(size) { size.getAvailableHeights() }
            when {
                availableHeights.isNotEmpty() -> {
                    val availableHeightsString = remember(availableHeights) {
                        availableHeights.reduce { acc, height -> "$acc, $height" }
                    }

                    Text(
                        text = stringResource(
                            id = R.string.uikit_available_heights_cm,
                            availableHeightsString,
                        ),
                        style = SizeSelectorDefaults.SizeMainTextStyle,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }

                size.isAvailable -> Unit

                else -> {
                    Text(
                        text = stringResource(RCommon.string.res_subscribe).uppercase(),
                        style = SizeSelectorDefaults.SizeAdditionalTextStyle,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                }
            }
        },
        modifier = modifier,
    )
}
