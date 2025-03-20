package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class ProductListNavActions(
    val onBackClicked: () -> Unit,
    val onSearchClicked: () -> Unit,
    val onFiltersClicked: (Category.Id, ProductFilters?) -> Unit,
    val onTagClicked: (Category, ProductFilters?) -> Unit,
    val onProductClicked: (Product) -> Unit,
    val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
) : NavigationActions
