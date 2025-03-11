package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.product.ProductShort

@Stable
internal sealed class ProductSuggestionsState {
    @Immutable
    data class Success(val products: ImmutableList<ProductShort>) : ProductSuggestionsState()

    data object None : ProductSuggestionsState()

    data object Loading : ProductSuggestionsState()

    data object Error : ProductSuggestionsState()
}
