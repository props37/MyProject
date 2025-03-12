package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

internal class SizeStateBuilder {
    fun build(offers: List<ProductOffer>, selectedOffer: ProductOffer?): SizeState {
        return if (offers.size > 1) {
            val heightSet = offers.mapTo(mutableSetOf()) { it.height }
            val isHeightVisible = heightSet.size > 1
            val sizes = offers
                .map { offer ->
                    Size(
                        offer = offer,
                        isHeightVisible = isHeightVisible,
                        isSelected = offer.barcode == selectedOffer?.barcode,
                    )
                }
                .toImmutableList()
            SizeState.Success(sizes)
        } else {
            SizeState.Empty
        }
    }
}
