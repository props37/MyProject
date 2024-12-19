package ru.livetyping.zarina.feature.product.ui.impl.impl.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.ProductState

@Composable
internal fun Product(
    productState: ProductState,
    onProductEvent: (ProductEvent) -> Unit,
    onShowZarinaClubDescription: () -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = productState,
        contentKey = {
            when (it) {
                is ProductState.Success -> ProductContentKey.Success
                is ProductState.Error -> it
                ProductState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is ProductState.Success -> {
                ProductSuccess(
                    productState = state,
                    onProductEvent = onProductEvent,
                    onShowZarinaClubDescription = onShowZarinaClubDescription,
                    lazyListState = lazyListState,
                )
            }

            ProductState.Loading -> {
                ProductSkeleton(modifier = Modifier.fillMaxWidth())
            }

            is ProductState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = { onProductEvent(ProductEvent.ErrorRefreshClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

private enum class ProductContentKey { Success }
