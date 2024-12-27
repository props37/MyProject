package ru.livetyping.zarina.data.store.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.data.store.impl.remote.api.StoreApi
import javax.inject.Inject

internal class StoreRemoteDataSourceImpl @Inject constructor(
    private val api: StoreApi,
) : StoreRemoteDataSource {
    override fun getStoresFlow(): Flow<List<Store>> = flow {
        val dto = api.getStores()
        val stores = dto
            .flatMap { country ->
                checkPropertyNotNull(country.name) { country::name }
                val cities = country.cities
                cities?.flatMap { city ->
                    city.getStores(country.name)
                } ?: emptyList()
            }
        emit(stores)
    }
}
