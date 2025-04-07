package ru.livetyping.zarina.data.checkout.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.data.checkout.impl.remote.api.CheckoutApi
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryOptionsDtoType
import javax.inject.Inject

internal class CheckoutRemoteDataSourceImpl @Inject constructor(
    private val api: CheckoutApi,
) : CheckoutRemoteDataSource {
    override fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>> = flow {
        val dto = api.getDeliveryMethods(cartType, cityKladrId)
        val methods = dto.mapNotNull { it.toDeliveryMethod() }
        emit(methods)
    }

    override fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPointShort>> = flow {
        val dto = api.getPickupPoints(cityKladrId)
        val pickupPoints = dto.mapNotNull { it.toPickupPointShort() }
        emit(pickupPoints)
    }

    override fun getPickupPointFlow(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetailed> = flow {
        val dto = api.getPickupPoint(cityKladrId, pickupPointId)
        emit(dto.toPickupPointDetails())
    }

    override fun getPickupStoresFlow(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType
    ): Flow<List<PickupStore>> = flow {
        val dto = api.getPickupStores(cityKladrId, deliveryMethodType)
        val stores = dto.mapNotNull { it.toPickupStore() }
        emit(stores)
    }

    override fun getCourierDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> = flow {
        val dto = api.getCourierDeliveryOptions(buildingKladrId)
        val options = dto.toDeliveryOptions(DeliveryOptionsDtoType.COURIER)
        emit(options)
    }

    override fun getPostDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> = flow {
        val dto = api.getPostDeliveryOptions(buildingKladrId)
        val options = dto.toDeliveryOptions(DeliveryOptionsDtoType.POST)
        emit(options)
    }
}
