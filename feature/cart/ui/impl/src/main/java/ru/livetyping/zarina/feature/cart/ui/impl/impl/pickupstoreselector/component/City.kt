package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun City(
    city: City,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(modifier = modifier) {
        Text(
            text = city.name,
            style = UiKitTheme.typography.secondary.bold,
        )
    }
}
