package ru.zarina.zarina.ui.screens.pickup.selectsize

import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import javax.inject.Inject

class SelectSizeInteractor @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
) {

    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

}
