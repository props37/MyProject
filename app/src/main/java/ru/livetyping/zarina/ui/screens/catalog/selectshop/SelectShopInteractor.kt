package ru.livetyping.zarina.ui.screens.catalog.selectshop

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.usecase.old.shop.GetShopsUseCase
import ru.livetyping.zarina.usecase.old.user.GetCityUseCase
import ru.livetyping.zarina.util.base.usecase.invoke

@Factory
class SelectShopInteractor(
    private val getCityUseCase: GetCityUseCase,
    private val getShopsUseCase: GetShopsUseCase
) {
    suspend fun getUserCity() = getCityUseCase()
    suspend fun getShops(city: City) = getShopsUseCase(GetShopsUseCase.Params(city))
}
