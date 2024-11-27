package ru.livetyping.zarina.core.uikit.paging

import android.os.Parcelable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListErrorItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListLoaderItem

// TODO: [Low] Refactor?
public fun LazyListScope.zarinaPagingPrependItem(
    prependLoadState: LoadState,
    onRetryClicked: () -> Unit,
    animateItem: Boolean = true,
) {
    when (prependLoadState) {
        LoadState.Loading -> {
            item(
                key = Key.PrependLoader,
                contentType = ContentType.Loader,
            ) {
                val animateItemModifier = if (animateItem) {
                    Modifier.animateZarinaItem(this)
                } else Modifier

                ZarinaListLoaderItem(modifier = animateItemModifier)
            }
        }

        is LoadState.Error -> {
            item(
                key = Key.PrependError,
                contentType = ContentType.Error,
            ) {
                val animateItemModifier = if (animateItem) {
                    Modifier.animateZarinaItem(this)
                } else Modifier

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
public fun LazyListScope.zarinaPagingAppendItem(
    appendLoadState: LoadState,
    onRetryClicked: () -> Unit,
    animateItem: Boolean = true,
) {
    when (appendLoadState) {
        LoadState.Loading -> {
            item(
                key = Key.AppendLoader,
                contentType = ContentType.Loader,
            ) {
                val animateItemModifier = if (animateItem) {
                    Modifier.animateZarinaItem(this)
                } else Modifier

                ZarinaListLoaderItem(modifier = animateItemModifier)
            }
        }

        is LoadState.Error -> {
            item(
                key = Key.AppendError,
                contentType = ContentType.Error,
            ) {
                val animateItemModifier = if (animateItem) {
                    Modifier.animateZarinaItem(this)
                } else Modifier

                ZarinaListErrorItem(
                    onRetryClicked = onRetryClicked,
                    modifier = animateItemModifier,
                )
            }
        }

        is LoadState.NotLoading -> Unit
    }
}

@Parcelize
private enum class Key : Parcelable {
    PrependLoader,
    PrependError,
    AppendLoader,
    AppendError,
}

private enum class ContentType { Loader, Error }
