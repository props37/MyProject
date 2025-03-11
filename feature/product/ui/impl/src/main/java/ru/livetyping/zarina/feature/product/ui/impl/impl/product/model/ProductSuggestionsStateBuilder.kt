package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.exception.ProductNotFoundException

internal class ProductSuggestionsStateBuilder {
    fun build(
        productSuggestionsResult: Result<List<ProductShort>>,
        productSuggestionsLoadingState: FlowRequester.LoadingState,
    ): ProductSuggestionsState {
        return if (productSuggestionsLoadingState.isLoading()) {
            ProductSuggestionsState.Loading
        } else {
            productSuggestionsResult.fold(
                onSuccess = { products ->
                    if (products.isNotEmpty()) {
                        ProductSuggestionsState.Success(products.toImmutableList())
                    } else {
                        ProductSuggestionsState.None
                    }
                },
                onFailure = { t ->
                    when (t) {
                        is ProductNotFoundException -> ProductSuggestionsState.None
                        else -> ProductSuggestionsState.Error
                    }
                },
            )
        }
    }
}
