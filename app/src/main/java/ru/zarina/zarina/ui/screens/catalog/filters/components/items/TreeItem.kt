package ru.zarina.zarina.ui.screens.catalog.filters.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.TreeFilter
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun TreeItem(
    filterName: String,
    treeFilter: TreeFilter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = filterName,
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            modifier = Modifier.padding(end = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        val selectedItems = remember(treeFilter) { treeFilter.getSelectedOptimized() }
        if (selectedItems.isNotEmpty()) {
            Text(
                text = selectedItems.first().name + if (selectedItems.size > 1) " +${selectedItems.size - 1}" else "",
                style = UiKitTheme.typography.circle1718,
                color = UiKitTheme.colors.hint,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right_24),
            contentDescription = null,
            tint = UiKitTheme.colors.primaryContentColor,
        )
    }
}
