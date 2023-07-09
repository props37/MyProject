package ru.zarina.zarina.ui.screens.catalog.selectshop

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.shop.GetShopsUseCase

@Factory
class SelectShopInteractor(
    private val getShopsUseCase: GetShopsUseCase
) {
    suspend fun getShops(city: City) = getShopsUseCase(GetShopsUseCase.Params(city))
}