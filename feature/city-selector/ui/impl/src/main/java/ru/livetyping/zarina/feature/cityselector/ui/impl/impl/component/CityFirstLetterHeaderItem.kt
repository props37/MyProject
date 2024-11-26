package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CityListItem

@Composable
internal fun CityFirstLetterHeaderItem(
    item: CityListItem.CityFirstLetterHeaderItem,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        modifier = modifier.heightIn(min = 24.dp),
    ) {
        Text(
            text = item.letter.toString(),
            style = UiKitTheme.typography.primary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
        )
    }
}
