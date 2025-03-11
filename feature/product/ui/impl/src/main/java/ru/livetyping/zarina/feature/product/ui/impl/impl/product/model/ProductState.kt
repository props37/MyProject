package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class ProductState {
    @Immutable
    data class Success(
        val product: ProductDetailed,
        val totalLookState: ProductSuggestionsState,
        val similarProductsState: ProductSuggestionsState,
    ) : ProductState()

    data object Loading : ProductState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : ProductState()
}
