package ru.livetyping.zarina.data.store.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.store.remote.api.StoreApi
import ru.livetyping.zarina.domain.store.Store
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(
    private val api: StoreApi,
) {
    fun getStoresFlow(): Flow<List<Store>> = flow {
        val dto = api.getStores()
        val stores = dto
            .flatMap { country ->
                checkNotNull(country.name) { "country name is null" }
                val cities = country.cities
                cities?.flatMap { city ->
                    city.getStores(country.name)
                } ?: emptyList()
            }
        emit(stores)
    }
}
