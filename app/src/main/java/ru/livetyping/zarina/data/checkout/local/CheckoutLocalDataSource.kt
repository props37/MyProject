package ru.livetyping.zarina.data.checkout.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class CheckoutLocalDataSource @Inject constructor(
    private val pickupPointDataHolder: PickupPointDataHolder,
) {
    fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPoint>?> {
        return pickupPointDataHolder.getPickupPointsFlow(cityKladrId)
    }

    fun setPickupPoints(
        cityKladrId: KladrId,
        pickupPoints: List<PickupPoint>,
    ) {
        pickupPointDataHolder.setPickupPoints(cityKladrId, pickupPoints)
    }

    fun clear() {
        pickupPointDataHolder.clear()
    }
}
