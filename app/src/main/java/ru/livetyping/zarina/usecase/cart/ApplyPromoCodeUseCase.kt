package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class ApplyPromoCodeUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<ApplyPromoCodeUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val promoCode = params.promoCode.trim()
        Timber.v("Apply promo code: $promoCode")
        cartRepository.applyPromoCode(promoCode)
    }

    data class Params(val promoCode: String)
}
