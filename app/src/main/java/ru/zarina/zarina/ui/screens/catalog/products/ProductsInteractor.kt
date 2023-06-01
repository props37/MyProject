package ru.zarina.zarina.ui.screens.catalog.products

import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.usecase.catalog.GetProductsPageUseCase
import javax.inject.Inject

class ProductsInteractor @Inject constructor(
    private val getProductsPageUseCase: GetProductsPageUseCase,
) {

    suspend fun getProductsPage(category: Category, pageIndex: Int) =
        getProductsPageUseCase.invoke(GetProductsPageUseCase.Params(category, pageIndex))

}
