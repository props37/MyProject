package ru.zarina.zarina.ui.screens.product

import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import javax.inject.Inject

class ProductInteractor @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
) {
    suspend fun getProduct(id: String) = getProductUseCase(id)
}
