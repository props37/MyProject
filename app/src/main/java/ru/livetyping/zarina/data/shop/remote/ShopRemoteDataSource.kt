package ru.livetyping.zarina.data.shop.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.shop.remote.api.ShopApi
import ru.livetyping.zarina.domain.shop.Shop
import javax.inject.Inject

class ShopRemoteDataSource @Inject constructor(
    private val api: ShopApi,
) {
    fun getShopsFlow(): Flow<List<Shop>> = flow {
        val dto = api.getShops()
        val shops = dto
            .flatMap { country ->
                val cities = country.cities
                checkNotNull(cities) { "cities is null" }
                cities
                    .mapNotNull { city ->
                        city.getShops()
                    }
                    .flatten()
            }
        emit(shops)
    }
}
