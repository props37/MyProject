package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.cart.CartProduct

@Stable
internal sealed class ProductCountSelectorState {
    data object None : ProductCountSelectorState()

    @Immutable
    data class ProductCountSelector(
        val product: CartProduct,
        val countItems: ImmutableList<ProductCountItem>,
    ) : ProductCountSelectorState()
}
