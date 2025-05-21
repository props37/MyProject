package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.text.withZarinaBrackets
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.resource.R as RCommon

internal fun LazyGridScope.loadMoreProductsButtonGridItem(
    onClick: () -> Unit,
) {
    item(
        key = Key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = ContentType,
    ) {
        LoadMoreProductsButton(
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun LoadMoreProductsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        TextButton(
            onClick = onClick,
            shape = RoundedCornerShape(1.dp),
            modifier = Modifier.padding(vertical = 32.dp),
        ) {
            Text(
                text = stringResource(RCommon.string.res_load_more).withZarinaBrackets().uppercase(),
                style = UiKitTheme2.typography.body,
                color = UiKitTheme2.colors.mainBlack,
            )
        }
    }
}

private const val Key = "LoadMoreProductsButton"
private const val ContentType = Key
