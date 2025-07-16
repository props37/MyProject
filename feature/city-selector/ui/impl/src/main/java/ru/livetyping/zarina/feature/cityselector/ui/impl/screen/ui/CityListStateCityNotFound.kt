package ru.livetyping.zarina.feature.cityselector.ui.impl.screen.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.feature.cityselector.ui.impl.R

@Composable
internal fun CityListStateCityNotFound(
    topPadding: Dp,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = PaddingValues(horizontal = HorizontalPadding),
        modifier = modifier.padding(top = topPadding, bottom = bottomPaddingProvider()),
    ) {
        Text(text = stringResource(R.string.city_selector_city_not_found_error_description).uppercase())
    }
}
