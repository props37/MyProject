package ru.zarina.zarina.ui.screens.pickup

import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import javax.inject.Inject

class PickupInteractor @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
) {

    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

}
