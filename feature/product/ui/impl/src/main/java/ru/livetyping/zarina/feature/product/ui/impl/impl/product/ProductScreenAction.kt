package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurements
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.SizeGuide

internal sealed interface ProductScreenAction {
    data object BackClicked : ProductScreenAction

    data class CheckAvailabilityInStoresClicked(val product: Product) : ProductScreenAction

    data class SubscribeToProductClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : ProductScreenAction

    data class ProductClicked(val product: Product) : ProductScreenAction

    data class SizeTableClicked(
        val measurements: ProductMeasurements,
        val modelInfo: ProductDetailed.ModelInfo?,
        val sizeGuide: SizeGuide,
        val gender: Gender,
    ) : ProductScreenAction
}
