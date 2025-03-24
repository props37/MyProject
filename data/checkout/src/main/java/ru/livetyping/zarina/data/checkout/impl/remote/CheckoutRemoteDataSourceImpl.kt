package ru.livetyping.zarina.data.checkout.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.data.checkout.impl.remote.api.CheckoutApi
import javax.inject.Inject

internal class CheckoutRemoteDataSourceImpl @Inject constructor(
    private val api: CheckoutApi,
) : CheckoutRemoteDataSource {
    override fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>> = flow {
        val dto = api.getDeliveryMethods(cartType, cityKladrId)
        val methods = dto.map { it.toDeliveryMethod() }
        emit(methods)
    }

    override fun getPickupStoresFlow(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType
    ): Flow<List<PickupStore>> = flow {
        val dto = api.getPickupStores(cityKladrId, deliveryMethodType)
        val stores = dto.map { it.toPickupStore() }
        emit(stores)
    }
}
