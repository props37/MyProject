package ru.livetyping.zarina.data.checkout

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.data.checkout.local.CheckoutLocalDataSource
import ru.livetyping.zarina.data.checkout.remote.CheckoutRemoteDataSource
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.domain.checkout.DeliveryOption
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class CheckoutRepository @Inject constructor(
    private val remoteDataSource: CheckoutRemoteDataSource,
    private val localDataSource: CheckoutLocalDataSource,
) {
    fun getPickupStoresFlow(cityKladrId: KladrId): Flow<List<PickupStore>> {
        return remoteDataSource.getPickupStoresFlow(cityKladrId)
    }

    fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>> {
        return remoteDataSource.getDeliveryMethodsFlow(cartType, cityKladrId)
    }

    fun getCourierDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> {
        return remoteDataSource.getCourierDeliveryOptionsFlow(buildingKladrId)
    }

    fun getPostDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> {
        return remoteDataSource.getPostDeliveryOptionsFlow(buildingKladrId)
    }

    fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPoint>> {
        return localDataSource.getPickupPointsFlow(cityKladrId)
            .onEach { cachedPickupPoints ->
                if (cachedPickupPoints == null) {
                    val pickupPoints =
                        remoteDataSource.getPickupPointsFlow(cityKladrId).firstOrNull()
                    checkNotNull(pickupPoints) { "Failed to fetch pickup points" }
                    localDataSource.setPickupPoints(cityKladrId, pickupPoints)
                }
            }
            .filterNotNull()
    }

    fun getPickupPointDetailsFlow(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetails> {
        return remoteDataSource.getPickupPointDetailsFlow(cityKladrId, pickupPointId)
    }

    fun clear() {
        localDataSource.clear()
    }
}
