package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.analytics.model.Screen
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uikit.product.ProductCard
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.SuggestionListState

@Composable
internal fun SuggestionListSuccess(
    state: SuggestionListState.Success,
    title: String,
    onProductClicked: (Product) -> Unit,
    onAddToWishlistClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val shimmer = rememberZarinaSkeletonShimmer()

    DisposableEffect(state.products) {
        listState.requestScrollToItem(0)
        onDispose {}
    }

    Column(modifier = modifier) {
        SuggestionListTitle(title)

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(
                items = state.products,
                key = { it.id.value },
            ) { product ->
                ProductCard(
                    product = product,
                    onClick = onProductClicked,
                    onAddToWishlistClicked = onAddToWishlistClicked,
                    isMediaScrollable = false,
                    mediaShimmer = shimmer,
                    appMetricaScreen = Screen.Product,
                    modifier = Modifier.fillParentMaxWidth(ProductCardWidthFraction),
                )
            }
        }
    }
}

private const val ProductCardWidthFraction = 0.465f
