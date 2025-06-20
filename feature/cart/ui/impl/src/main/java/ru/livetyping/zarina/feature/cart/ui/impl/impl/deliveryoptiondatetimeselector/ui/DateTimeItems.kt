package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.icon.ZarinaCheckmarkIcon
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.model.DateTimeItem

@Composable
internal fun DateTimeItems(
    items: ImmutableList<DateTimeItem>,
    onItemClicked: (DateTimeItem) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    val windowInsetsBottomPadding =
        windowInsetsProvider().asPaddingValues().calculateBottomPadding()
    val contentPadding = PaddingValues(
        bottom = windowInsetsBottomPadding + ZarinaScrollableDefaults.ScrollableBottomPadding,
    )

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.dateTimePeriod.id.value },
        ) { index, item ->
            ZarinaItem(
                onClick = { onItemClicked(item) },
                startContent = {
                    Text(
                        text = item.text.uppercase(),
                        style = UiKitTheme2.typography.body,
                    )
                },
                endContent = {
                    ZarinaCheckmarkIcon(
                        isVisible = item.isSelected,
                        iconSize = 16.dp,
                    )
                },
            )

            if (index != items.lastIndex) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}
