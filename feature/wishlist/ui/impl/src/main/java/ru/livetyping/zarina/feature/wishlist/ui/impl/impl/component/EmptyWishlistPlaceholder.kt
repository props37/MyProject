package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorButtonState
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorScreenState
import ru.livetyping.zarina.feature.wishlist.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun EmptyWishlistPlaceholder(
    onGoToCatalogClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberZarinaErrorScreenState(
        iconResId = RCommon.drawable.ic_heart_outline_64,
        title = stringResource(R.string.wishlist_empty_placeholder_title),
        body = stringResource(R.string.wishlist_empty_placeholder_body),
        buttonState = rememberZarinaErrorButtonState(
            buttonText = stringResource(R.string.wishlist_go_to_catalog),
        )
    )

    ZarinaErrorScreen(
        state = state,
        onButtonClicked = onGoToCatalogClicked,
        modifier = modifier,
    )
}
