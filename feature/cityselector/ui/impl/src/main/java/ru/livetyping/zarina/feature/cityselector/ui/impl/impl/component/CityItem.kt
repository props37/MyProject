package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uikit.icon.ZarinaCheckmarkIcon
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListItem

@Composable
internal fun CityItem(
    item: CityListItem.CityItem,
    isCitySelected: Boolean,
    onCityClicked: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    val city = item.city

    ZarinaItem(
        onClick = { onCityClicked(city) },
        startContent = {
            Column {
                Text(
                    text = city.name,
                    style = CityNameTextStyle,
                    color = UiKitTheme.colors.text.general.regular.default,
                )

                if (item.showFullName) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = city.fullName ?: city.name,
                        style = UiKitTheme.typography.footnote.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }
            }
        },
        endContent = {
            ZarinaCheckmarkIcon(
                isVisible = isCitySelected,
                iconSize = 16.dp,
                modifier = Modifier.padding(start = if (isCitySelected) 16.dp else 0.dp),
            )
        },
        modifier = modifier,
    )
}

@Composable
internal fun CityItemSkeleton(
    skeletonWidthFraction: Float,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(modifier = modifier) {
        ZarinaTextSkeleton(
            textStyle = CityNameTextStyle,
            modifier = Modifier.fillMaxWidth(skeletonWidthFraction),
        )
    }
}

private val CityNameTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light
