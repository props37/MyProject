package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uicompose.getHorizontalPaddingValues
import ru.livetyping.zarina.core.uicompose.getVerticalPaddingValues
import ru.livetyping.zarina.core.uikit.list.ZarinaListErrorItem
import ru.livetyping.zarina.core.uikit.product.ProductCardSmall
import ru.livetyping.zarina.core.uikit.product.ProductCardSmallSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductSuggestionsEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductSuggestionsState

@Composable
internal fun ProductSuggestions(
    title: String,
    state: ProductSuggestionsState,
    onEvent: (ProductSuggestionsEvent) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
) {
    Column(
        modifier = modifier.padding(contentPadding.getVerticalPaddingValues()),
    ) {
        val horizontalPadding =
            contentPadding.getHorizontalPaddingValues(LocalLayoutDirection.current)

        Text(
            text = title,
            style = UiKitTheme.typography.secondary.bold,
            modifier = Modifier.padding(horizontalPadding),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is ProductSuggestionsState.Success -> ContentKey.Success
                    ProductSuggestionsState.Loading -> it
                    ProductSuggestionsState.Error -> it
                    ProductSuggestionsState.None -> it
                }
            },
        ) { state ->
            when (state) {
                is ProductSuggestionsState.Success -> {
                    ProductSuggestionsSuccess(
                        state = state,
                        onProductClicked = { onEvent(ProductSuggestionsEvent.ProductClicked(it)) },
                        contentPadding = horizontalPadding,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                ProductSuggestionsState.Loading -> {
                    ProductSuggestionsLoading(contentPadding = horizontalPadding)
                }

                ProductSuggestionsState.Error -> {
                    ZarinaListErrorItem(
                        onRetryClicked = { onEvent(ProductSuggestionsEvent.ErrorRefreshClicked) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                ProductSuggestionsState.None -> Unit
            }
        }
    }
}

@Composable
private fun ProductSuggestionsSuccess(
    state: ProductSuggestionsState.Success,
    onProductClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyRow(
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(ProductCardSpacedBy),
        modifier = modifier,
    ) {
        items(
            items = state.products,
            key = { it.id.value },
        ) { product ->
            ProductCardSmall(
                product = product,
                onClick = onProductClicked,
                modifier = Modifier.width(ProductCardWidth),
            )
        }
    }
}

@Composable
private fun ProductSuggestionsLoading(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    Row(modifier = modifier.padding(contentPadding.getVerticalPaddingValues())) {
        val layoutDirection = LocalLayoutDirection.current
        Spacer(modifier = Modifier.width(contentPadding.calculateStartPadding(layoutDirection)))

        repeat(LoadingItemCount) { index ->
            ProductCardSmallSkeleton(
                shimmer = shimmer,
                modifier = Modifier.width(ProductCardWidth),
            )

            if (index < LoadingItemCount - 1) {
                Spacer(modifier = Modifier.width(ProductCardSpacedBy))
            }
        }

        Spacer(modifier = Modifier.width(contentPadding.calculateEndPadding(layoutDirection)))
    }
}

private enum class ContentKey { Success }

private val ProductCardWidth = 176.dp
private val ProductCardSpacedBy = 12.dp

private const val LoadingItemCount = 4
