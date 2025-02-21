package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductColorFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSortFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.sorting
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uicompose.toComposeColor
import ru.livetyping.zarina.core.uikit.color.ZarinaColorIcon
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.icon.ZarinaCheckmarkIcon
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun ListFilterItems(
    filter: ProductListFilter<ProductListFilterItem>,
    onItemClicked: (ProductListFilterItem) -> Unit,
    cityHeader: City?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        if (cityHeader != null) {
            item(key = cityHeader.id.value) {
                ZarinaItem(modifier = Modifier.animateZarinaItem(this)) {
                    Text(
                        text = cityHeader.name,
                        style = UiKitTheme.typography.secondary.bold,
                    )
                }
            }
        }

        itemsIndexed(
            items = filter.items,
            key = { _, item -> item.id.value }
        ) { index, item ->
            Column(modifier = Modifier.animateZarinaItem(this)) {
                FilterItem(
                    item = item,
                    onItemClicked = onItemClicked,
                )

                if (index < filter.items.lastIndex) {
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
private fun FilterItem(
    item: ProductListFilterItem,
    onItemClicked: (ProductListFilterItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clickable { onItemClicked(item) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        if (item is ProductColorFilterItem && item.color != null) {
            ZarinaColorIcon(
                color = item.color?.toComposeColor() ?: Color.Unspecified,
                size = 16.dp,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        val name = if (item is ProductSortFilterItem) {
            stringResource(item.sorting.nameResId)
        } else {
            item.name
        }

        Text(
            text = name,
            style = UiKitTheme.typography.secondary.light,
            color = UiKitTheme.colors.text.general.regular.default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))

        ZarinaCheckmarkIcon(
            isVisible = item.isSelected,
            iconSize = 16.dp,
            modifier = Modifier.padding(start = if (item.isSelected) 16.dp else 0.dp),
        )
    }
}
