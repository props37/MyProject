package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState2

@Stable
internal sealed class ProductState {
    @Immutable
    data class Success(
        val product: ProductDetailed,
        val labels: ImmutableList<String>?,
        val mediaBanner: Media?,
        val totalLookProductState: SuggestionListState,
        val similarProductState: SuggestionListState,
        val selectedSize: String?,
        val selectedHeight: String?,
        val shouldSelectHeight: Boolean,
    ) : ProductState()

    data object Loading : ProductState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState2) : ProductState()

    class Builder {
        fun build(
            productResult: Result<ProductDetailed>?,
            isProductLoading: Boolean,
            totalLookProductState: SuggestionListState,
            similarProductState: SuggestionListState,
            selectedSize: String?,
            selectedHeight: String?,
            shouldSelectHeight: Boolean,
        ): ProductState {
            return if (productResult == null || isProductLoading) {
                Loading
            } else {
                productResult.fold(
                    onSuccess = { product ->
                        val mediaBanner = product.media
                            .drop(1)
                            .firstOrNull { it.type == MediaType.IMAGE }

                        Success(
                            product = product,
                            labels = buildLabelList(product)?.toImmutableList(),
                            mediaBanner = mediaBanner,
                            totalLookProductState = totalLookProductState,
                            similarProductState = similarProductState,
                            selectedSize = selectedSize,
                            selectedHeight = selectedHeight,
                            shouldSelectHeight = shouldSelectHeight,
                        )
                    },
                    onFailure = { t ->
                        val errorState = ZarinaErrorScreenState2.from(t)
                        Error(errorState)
                    },
                )
            }
        }

        private fun buildLabelList(product: ProductDetailed): List<String>? {
            return buildList {
                val discount = product.price.discount
                if (discount != null) {
                    add(DISCOUNT_FORMAT.format(discount.discountPercent.toString()))
                }
                product.label?.let { label ->
                    add(label.name)
                }
            }.takeIf { it.isNotEmpty() }
        }

        private companion object {
            private const val DISCOUNT_FORMAT = "-%1\$s%%"
        }
    }
}
