package ru.livetyping.zarina.presentation.common.component.paging

import androidx.compose.foundation.lazy.LazyListScope
import androidx.paging.LoadState
import ru.livetyping.zarina.presentation.common.component.list.ZarinaListErrorItem
import ru.livetyping.zarina.presentation.common.component.list.ZarinaListLoaderItem

fun LazyListScope.zarinaPagingPrependItem(
    prependLoadState: LoadState,
    onRetryClicked: () -> Unit,
) {
    when (prependLoadState) {
        LoadState.Loading -> {
            item(
                key = KeyPrependLoader,
                contentType = ContentTypeLoader,
            ) {
                ZarinaListLoaderItem()
            }
        }

        is LoadState.Error -> {
            item(
                key = KeyPrependError,
                contentType = ContentTypeError,
            ) {
                ZarinaListErrorItem(onRetryClicked = onRetryClicked)
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

fun LazyListScope.zarinaPagingAppendItem(
    appendLoadState: LoadState,
    onRetryClicked: () -> Unit,
) {
    when (appendLoadState) {
        LoadState.Loading -> {
            item(
                key = KeyAppendLoader,
                contentType = ContentTypeLoader,
            ) {
                ZarinaListLoaderItem()
            }
        }

        is LoadState.Error -> {
            item(
                key = KeyAppendError,
                contentType = ContentTypeError,
            ) {
                ZarinaListErrorItem(onRetryClicked = onRetryClicked)
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
