package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.CartType
import timber.log.Timber
import javax.inject.Inject

class RemoveMyCardFromCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<RemoveMyCardFromCartUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val cartType = params.cartType
        Timber.v("Remove MyCard from cart $cartType")
        cartRepository.removeMyCardFromCart(cartType)
    }

    data class Params(val cartType: CartType)
}
