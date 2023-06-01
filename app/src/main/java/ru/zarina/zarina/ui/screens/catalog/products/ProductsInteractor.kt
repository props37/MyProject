package ru.zarina.zarina.ui.screens.catalog.products

import ru.zarina.zarina.data.category.ICategoryRepository
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.usecase.catalog.GetProductsPageUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    private val categoryRepository: ICategoryRepository,
    val getProductsPageUseCase: GetProductsPageUseCase,
) {

    fun getCategory(id: Category.Id) = categoryRepository.getCategory(id)

}
