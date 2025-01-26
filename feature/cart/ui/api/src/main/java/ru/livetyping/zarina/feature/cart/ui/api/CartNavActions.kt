package ru.livetyping.zarina.feature.cart.ui.api

import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.navigation.NavigationActions

public class CartNavActions(
    public val onBackClicked: () -> Unit,
    public val onChangeCityClicked: (City?) -> Unit,
    public val onGoToCatalogClicked: () -> Unit,
    public val onProductClicked: (CartProduct) -> Unit,
) : NavigationActions
