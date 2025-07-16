package ru.livetyping.zarina.feature.catalog.ui.impl.screen

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface CatalogScreenAction {
    data object BackClicked : CatalogScreenAction

    data object SearchClicked : CatalogScreenAction

    data class CategoryClicked(val categoryId: Category.Id) : CatalogScreenAction

    data class UrlClicked(val url: Url) : CatalogScreenAction

    data class CityClicked(val currentCity: City) : CatalogScreenAction
}
