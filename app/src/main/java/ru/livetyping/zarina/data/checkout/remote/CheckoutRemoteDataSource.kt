package ru.livetyping.zarina.data.checkout.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.checkout.remote.api.CheckoutApi
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class CheckoutRemoteDataSource @Inject constructor(
    private val api: CheckoutApi,
) {
    fun getStoresFlow(cityKladrId: KladrId): Flow<List<PickupStore>> = flow {
        val dto = api.getPickupStores(cityKladrId)
        val stores = dto.map { it.toStore() }
        emit(stores)
    }
}
