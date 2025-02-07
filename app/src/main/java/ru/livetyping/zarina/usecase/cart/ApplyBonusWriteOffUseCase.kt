package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.CartType
import timber.log.Timber
import javax.inject.Inject

class ApplyBonusWriteOffUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<ApplyBonusWriteOffUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val cartType = params.cartType
        val bonusCount = params.bonusCount
        Timber.v("Apply bonus write off for cart $cartType. Bonus count: $bonusCount")
        cartRepository.applyBonusWriteOff(cartType, bonusCount)
        AppMetricaHelper.reportBonusesUsed(bonusCount)
    }

    data class Params(
        val cartType: CartType,
        val bonusCount: Int,
    )
}
