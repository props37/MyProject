package ru.livetyping.zarina.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun <T> FilterBar(
    sort: T,
    sortName: @Composable (T) -> String,
    onSortClick: () -> Unit,
    isFilterButtonEnabled: Boolean,
    onFiltersClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .height(IntrinsicSize.Min)
            .border(
                width = 1.dp,
                color = UiKitTheme.colorsOld.primaryBorderColor,
            ),
    ) {
        FilterButton(
            icon = R.drawable.old_ic_sort_24,
            text = sortName(sort),
            onClick = onSortClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(UiKitTheme.colorsOld.listDivider)
        )
        FilterButton(
            isEnabled = isFilterButtonEnabled,
            icon = R.drawable.old_ic_sliders_24,
            text = stringResource(R.string.filters),
            onClick = onFiltersClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}
