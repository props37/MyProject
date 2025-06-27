package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductMeasurements
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.SizeGuide
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class ProductNavActions(
    val onBackClicked: () -> Unit,
    val onCheckAvailabilityInStoresClicked: (Product) -> Unit,
    val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
    val onProductClicked: (Product) -> Unit,
    val onSizeTableClicked: (ProductMeasurements, SizeGuide, Gender) -> Unit,
) : NavigationActions
