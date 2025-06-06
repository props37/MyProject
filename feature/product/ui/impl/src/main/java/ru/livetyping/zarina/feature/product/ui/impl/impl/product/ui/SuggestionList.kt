package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SuggestionListState

@Suppress("NAME_SHADOWING")
@Composable
internal fun SuggestionList(
    state: SuggestionListState,
    title: String,
    onProductClicked: (Product) -> Unit,
    onAddToWishlistClicked: (Product) -> Unit,
    onErrorRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is SuggestionListState.Success -> SuggestionListContentKey.Success
                SuggestionListState.Empty -> it
                SuggestionListState.Error -> it
                SuggestionListState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is SuggestionListState.Success -> {
                SuggestionListSuccess(
                    state = state,
                    title = title,
                    onProductClicked = onProductClicked,
                    onAddToWishlistClicked = onAddToWishlistClicked,
                )
            }

            SuggestionListState.Loading -> {
                // TODO: [Top] Implement
            }

            SuggestionListState.Error -> {
                SuggestionListError(
                    title = title,
                    onRetryClicked = onErrorRetryClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }

            SuggestionListState.Empty -> Unit
        }
    }
}

private enum class SuggestionListContentKey { Success }
