package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorButtonState
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorScreenState
import ru.livetyping.zarina.feature.productlist.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun EmptyProductsPlaceholder(
    modifier: Modifier = Modifier,
) {
    val state = rememberZarinaErrorScreenState(
        iconResId = RCommon.drawable.ic_magnifying_glass_64,
        title = stringResource(R.string.product_list_could_not_find_products),
        body = stringResource(R.string.product_list_try_select_another_category),
        buttonState = rememberZarinaErrorButtonState(isButtonVisible = false),
    )

    ZarinaErrorScreen(
        state = state,
        onButtonClicked = {},
        modifier = modifier,
    )
}
