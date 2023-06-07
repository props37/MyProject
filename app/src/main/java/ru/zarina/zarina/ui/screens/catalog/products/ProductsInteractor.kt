package ru.zarina.zarina.ui.screens.catalog.products

import ru.zarina.zarina.data.category.ICategoryRepository
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.ui.screens.catalog.CatalogCoordinator
import ru.zarina.zarina.usecase.catalog.GetProductsPageUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    private val coordinator: CatalogCoordinator,
    private val categoryRepository: ICategoryRepository,
    val getProductsPageUseCase: GetProductsPageUseCase,
) {

    val sort
        get() = coordinator.sort

    fun getCategory(id: Category.Id) = categoryRepository.getCategory(id)

}
