package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.City

internal class CartNavActions(
    val onBackClicked: () -> Unit,
    val onChangeCityClicked: (City?) -> Unit,
    val onGoToCatalogClicked: () -> Unit,
    val onProductClicked: (CartProduct) -> Unit,
    val onCheckoutClicked: (CartType) -> Unit,
)
