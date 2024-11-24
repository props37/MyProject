package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.times
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.catalog.ui.impl.R
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListItem

@Composable
internal fun SeeWholeCategoryItem(
    item: CategoryListItem.SeeWholeCategoryItem,
    onItemClicked: (CategoryListItem.SeeWholeCategoryItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = { onItemClicked(item) },
        modifier = modifier,
    ) {
        val nestingStartPadding =
            item.nestingLevel * CategoryItemDefaults.NestingStartPaddingPerLevel

        Text(
            text = stringResource(R.string.catalog_see_everything).uppercase(),
            style = UiKitTheme.typography.tertiary.light,
            color = UiKitTheme.colors.text.general.regular.default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = nestingStartPadding),
        )
    }
}
