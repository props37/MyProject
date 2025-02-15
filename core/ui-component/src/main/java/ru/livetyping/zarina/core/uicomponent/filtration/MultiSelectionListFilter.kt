package ru.livetyping.zarina.core.uicomponent.filtration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uikit.counter.ZarinaCounter
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun MultiSelectionListFilter(
    type: ProductFilter.Type,
    selectedCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = FilterMinHeight)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(type.nameResId),
            style = FilterTitleTextStyle,
            color = FilterTitleColor,
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (selectedCount > 0) {
            ZarinaCounter(
                value = selectedCount.toString(),
                textStyle = UiKitTheme.typography.footnote.bold,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))

        FilterEndArrowIcon()
    }
}
