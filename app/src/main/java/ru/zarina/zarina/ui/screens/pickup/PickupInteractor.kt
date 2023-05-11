package ru.zarina.zarina.ui.screens.pickup

import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import ru.zarina.zarina.usecase.user.GetCityUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class PickupInteractor @Inject constructor(
    private val getCityUseCase: GetCityUseCase,
    private val getProductUseCase: GetProductUseCase,
) {

    suspend fun getCity() = getCityUseCase()

    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

}
