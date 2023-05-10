package ru.zarina.zarina.ui.screens.pickup.root

import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import javax.inject.Inject

class PickupRootInteractor @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
) {

    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

}
