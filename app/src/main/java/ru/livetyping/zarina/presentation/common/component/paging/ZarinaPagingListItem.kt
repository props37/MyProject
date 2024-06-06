package ru.livetyping.zarina.presentation.common.component.paging

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import ru.livetyping.zarina.presentation.common.component.list.ZarinaListErrorItem
import ru.livetyping.zarina.presentation.common.component.list.ZarinaListLoaderItem

// TODO: [Low] Refactor?
fun LazyListScope.zarinaPagingPrependItem(
    prependLoadState: LoadState,
    onRetryClicked: () -> Unit,
    animateItem: Boolean = true,
) {
    when (prependLoadState) {
        LoadState.Loading -> {
            item(
                key = KeyPrependLoader,
                contentType = ContentTypeLoader,
            ) {
                val animateItemModifier = if (animateItem) Modifier.animateItem() else Modifier
                ZarinaListLoaderItem(modifier = animateItemModifier)
            }
        }

        is LoadState.Error -> {
            item(
                key = KeyPrependError,
                contentType = ContentTypeError,
            ) {
                val animateItemModifier = if (animateItem) Modifier.animateItem() else Modifier
                ZarinaListErrorItem(
                    onRetryClicked = onRetryClicked,
                    modifier = animateItemModifier,
                )
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

// TODO: [Low] Refactor?
fun LazyListScope.zarinaPagingAppendItem(
    appendLoadState: LoadState,
    onRetryClicked: () -> Unit,
    animateItem: Boolean = true,
) {
    when (appendLoadState) {
        LoadState.Loading -> {
            item(
                key = KeyAppendLoader,
                contentType = ContentTypeLoader,
            ) {
                val animateItemModifier = if (animateItem) Modifier.animateItem() else Modifier
                ZarinaListLoaderItem(modifier = animateItemModifier)
            }
        }

        is LoadState.Error -> {
            item(
                key = KeyAppendError,
                contentType = ContentTypeError,
            ) {
                val animateItemModifier = if (animateItem) Modifier.animateItem() else Modifier
                ZarinaListErrorItem(
                    onRetryClicked = onRetryClicked,
                    modifier = animateItemModifier,
                )
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

private const val KeyPrependLoader = "ZarinaPagingPrependLoaderKey"
private const val KeyPrependError = "ZarinaPagingPrependErrorKey"
private const val KeyAppendLoader = "ZarinaPagingAppendLoaderKey"
private const val KeyAppendError = "ZarinaPagingAppendErrorKey"

private const val ContentTypeLoader = "ZarinaPagingLoaderContentType"
private const val ContentTypeError = "ZarinaPagingErrorContentType"
