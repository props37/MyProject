package ru.zarina.zarina.ui.screens.product

import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.catalog.GetCompleteLookUseCase
import ru.zarina.zarina.usecase.catalog.GetDeliveryAvailabilityUseCase
import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import javax.inject.Inject

class ProductInteractor @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getCompleteLookUseCase: GetCompleteLookUseCase,
    private val getDeliveryAvailabilityUseCase: GetDeliveryAvailabilityUseCase,
) {
    suspend fun getProduct(id: String) = getProductUseCase(id)

    suspend fun getCompleteLook(product: Product) =
        getCompleteLookUseCase(GetCompleteLookUseCase.Params(product))

    suspend fun getDeliveryAvailability(product: Product) =
        getDeliveryAvailabilityUseCase(GetDeliveryAvailabilityUseCase.Params(product))
}
