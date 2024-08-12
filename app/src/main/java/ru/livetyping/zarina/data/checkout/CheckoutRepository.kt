package ru.livetyping.zarina.data.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.checkout.remote.CheckoutRemoteDataSource
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class CheckoutRepository @Inject constructor(
    private val remoteDataSource: CheckoutRemoteDataSource,
) {
    fun getPickupStores(cityKladrId: KladrId): Flow<List<PickupStore>> {
        return remoteDataSource.getStoresFlow(cityKladrId)
    }
}
