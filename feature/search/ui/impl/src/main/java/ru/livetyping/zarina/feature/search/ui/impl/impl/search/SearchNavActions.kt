package ru.livetyping.zarina.feature.search.ui.impl.impl.search

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class SearchNavActions(
    val onBackClicked: () -> Unit,
    val onFiltersClicked: (searchQuery: String, ProductFilters?) -> Unit,
    val onCategoryClicked: (Category.Id) -> Unit,
    val onProductClicked: (Product) -> Unit,
    val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
) : NavigationActions
