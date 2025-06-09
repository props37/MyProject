package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState2

@Stable
internal sealed class ProductState {
    @Immutable
    data class Success(
        val product: ProductDetailed,
        val mediaBanner: Media?,
        val totalLookProductState: SuggestionListState,
        val similarProductState: SuggestionListState,
    ) : ProductState()

    data object Loading : ProductState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState2) : ProductState()

    class Builder {
        private val suggestionListStateBuilder = SuggestionListState.Builder()

        fun build(
            productResult: Result<ProductDetailed>?,
            isProductLoading: Boolean,
            totalLookProductsResult: Result<List<ProductShort>>?,
            areTotalLookProductsLoading: Boolean,
            similarProductsResult: Result<List<ProductShort>>?,
            areSimilarProductsLoading: Boolean,
        ): ProductState {
            return if (productResult == null || isProductLoading) {
                Loading
            } else {
                productResult.fold(
                    onSuccess = { product ->
                        val mediaBanner = product.media
                            .drop(1)
                            .firstOrNull { it.type == MediaType.IMAGE }
                        val totalLookProductState = suggestionListStateBuilder.build(
                            result = totalLookProductsResult,
                            isLoading = areTotalLookProductsLoading,
                        )
                        val similarProductState = suggestionListStateBuilder.build(
                            result = similarProductsResult,
                            isLoading = areSimilarProductsLoading,
                        )

                        Success(
                            product = product,
                            mediaBanner = mediaBanner,
                            totalLookProductState = totalLookProductState,
                            similarProductState = similarProductState,
                        )
                    },
                    onFailure = { t ->
                        val errorState = ZarinaErrorScreenState2.from(t)
                        Error(errorState)
                    },
                )
            }
        }

        private companion object {
            private const val MEDIA_BANNER_INDEX = 1
        }
    }
}
