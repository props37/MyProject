package ru.zarina.zarina.ui.screens.pickup

import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import ru.zarina.zarina.usecase.shop.GetOffersUseCase
import ru.zarina.zarina.usecase.user.GetCityUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class PickupInteractor @Inject constructor(
    private val getCityUseCase: GetCityUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val getOffersUseCase: GetOffersUseCase,
) {

    suspend fun getCity() = getCityUseCase()

    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

    suspend fun getOffers(product: Product, city: City) =
        getOffersUseCase(GetOffersUseCase.Params(product, city))

}
