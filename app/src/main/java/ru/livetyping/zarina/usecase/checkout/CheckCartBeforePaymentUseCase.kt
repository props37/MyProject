package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.checkout.exception.CartChangedException
import javax.inject.Inject

class CheckCartBeforePaymentUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository,
) : UseCase<CheckCartBeforePaymentUseCase.Params, Cart>(dispatcher) {

    override suspend fun execute(params: Params): Cart {
        val checkoutParams = params.checkoutParams
        val paymentMethod = params.paymentMethod
        val targetCart = checkoutRepository.getCartFlow(checkoutParams, paymentMethod).firstOrNull()
        checkNotNull(targetCart) { "Failed to get cart" }

        val cart = params.cart
        if (targetCart != cart) {
            throw CartChangedException()
        }
        return cart
    }

    data class Params(
        val cart: Cart,
        val paymentMethod: PaymentMethod,
        val checkoutParams: CheckoutParams,
    )
}
