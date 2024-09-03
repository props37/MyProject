package ru.livetyping.zarina.data.checkout.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.geography.KladrId
import java.lang.ref.SoftReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PickupPointDataHolder @Inject constructor() {
    private val pickupPoints =
        MutableStateFlow<SoftReference<List<PickupPoint>>?>(SoftReference(null))

    @Volatile
    private var pickupPointsCityKladrId: KladrId? = null

    @Synchronized
    fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPoint>?> {
        return if (cityKladrId == pickupPointsCityKladrId) {
            pickupPoints.map { it?.get() }
        } else {
            flowOf(null)
        }
    }

    @Synchronized
    fun setPickupPoints(
        cityKladrId: KladrId,
        pickupPoints: List<PickupPoint>,
    ) {
        this.pickupPoints.value = SoftReference(pickupPoints)
        pickupPointsCityKladrId = cityKladrId
    }

    @Synchronized
    fun clear() {
        pickupPoints.value = null
        pickupPointsCityKladrId = null
    }
}
