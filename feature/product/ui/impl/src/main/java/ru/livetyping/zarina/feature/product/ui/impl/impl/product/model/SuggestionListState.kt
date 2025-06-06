package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.exception.ProductNotFoundException

@Stable
internal sealed class SuggestionListState {
    @Immutable
    data class Success(val products: ImmutableList<ProductShort>) : SuggestionListState()

    data object Empty : SuggestionListState()

    data object Loading : SuggestionListState()

    data object Error : SuggestionListState()

    class Builder {
        fun build(
            result: Result<List<ProductShort>>?,
            isLoading: Boolean,
        ): SuggestionListState {
            return if (result == null || isLoading) {
                Loading
            } else {
                result.fold(
                    onSuccess = { products ->
                        if (products.isNotEmpty()) {
                            Success(products.toImmutableList())
                        } else {
                            Empty
                        }
                    },
                    onFailure = { t ->
                        if (t is ProductNotFoundException) {
                            Empty
                        } else {
                            Error
                        }
                    }
                )
            }
        }
    }
}
