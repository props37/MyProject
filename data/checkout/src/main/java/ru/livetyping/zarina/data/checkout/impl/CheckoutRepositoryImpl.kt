package ru.livetyping.zarina.data.checkout.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.data.checkout.impl.remote.CheckoutRemoteDataSource
import javax.inject.Inject

internal class CheckoutRepositoryImpl @Inject constructor(
    private val remoteDataSource: CheckoutRemoteDataSource,
) : CheckoutRepository {
    override fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId
    ): Flow<List<DeliveryMethod>> {
        return remoteDataSource.getDeliveryMethodsFlow(cartType, cityKladrId)
    }

    override fun getPickupStoresFlow(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType
    ): Flow<List<PickupStore>> {
        return remoteDataSource.getPickupStoresFlow(cityKladrId, deliveryMethodType)
    }
}
