package ru.zarina.zarina.ui.screens.catalog.products

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.category.ICategoryRepository
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.ui.screens.catalog.CatalogCoordinator
import ru.zarina.zarina.usecase.catalog.GetProductsPageUseCase

@Factory
class ProductsInteractor(
    private val coordinator: CatalogCoordinator,
    private val categoryRepository: ICategoryRepository,
    val getProductsPageUseCase: GetProductsPageUseCase,
) {

    val filtration
        get() = coordinator.filtration

    fun getCategory(id: Category.Id) = categoryRepository.getCategory(id)

}
