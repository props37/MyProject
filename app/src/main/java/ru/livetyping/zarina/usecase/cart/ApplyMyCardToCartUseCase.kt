package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.DeliveryType
import timber.log.Timber
import javax.inject.Inject

class ApplyMyCardToCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<ApplyMyCardToCartUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val deliveryType = params.deliveryType
        val productsFirstPriceSum = params.productsFirstPriceSum
        Timber.v("Apply MyCard to cart $deliveryType. Product first price sum: $productsFirstPriceSum")
        cartRepository.applyMyCardToCart(deliveryType, productsFirstPriceSum)
    }

    data class Params(
        val deliveryType: DeliveryType,
        val productsFirstPriceSum: Int,
    )
}
