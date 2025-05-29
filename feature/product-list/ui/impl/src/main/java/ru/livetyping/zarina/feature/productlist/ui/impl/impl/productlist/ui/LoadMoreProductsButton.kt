package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListLoadMoreButton

internal fun LazyGridScope.loadMoreProductsButtonGridItem(
    onClick: () -> Unit,
) {
    item(
        key = Key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = ContentType,
    ) {
        ZarinaListLoadMoreButton(
            onClick = onClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .animateZarinaItem(this),
        )
    }
}

private const val Key = "LoadMoreProductsButton"
private const val ContentType = Key
