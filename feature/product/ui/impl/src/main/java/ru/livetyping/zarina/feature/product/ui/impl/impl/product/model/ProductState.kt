package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState2

@Stable
internal sealed class ProductState {
    @Immutable
    data class Success(val product: ProductDetailed) : ProductState()

    data object Loading : ProductState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState2) : ProductState()

    class Builder {
        fun build(
            productResult: Result<ProductDetailed>?,
            isProductLoading: Boolean,
        ): ProductState {
            return if (productResult == null || isProductLoading) {
                Loading
            } else {
                productResult.fold(
                    onSuccess = { product ->
                        Success(product)
                    },
                    onFailure = { t ->
                        val errorState = ZarinaErrorScreenState2.from(t)
                        Error(errorState)
                    },
                )
            }
        }
    }
}
