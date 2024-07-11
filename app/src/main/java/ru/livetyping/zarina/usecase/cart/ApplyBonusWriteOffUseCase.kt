package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.DeliveryType
import timber.log.Timber
import javax.inject.Inject

class ApplyBonusWriteOffUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<ApplyBonusWriteOffUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val deliveryType = params.deliveryType
        val bonusCount = params.bonusCount
        Timber.v("Apply bonus write off for cart $deliveryType. Bonus count: $bonusCount")
        cartRepository.applyBonusWriteOff(deliveryType, bonusCount)
    }

    data class Params(
        val deliveryType: DeliveryType,
        val bonusCount: Int,
    )
}
