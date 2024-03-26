package ru.zarina.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.cart.CartRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.product.Product
import timber.log.Timber
import javax.inject.Inject

class ChangeProductCountInCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<ChangeProductCountInCartUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val productId = params.productId
        val barcode = params.barcode
        val count = params.count
        val deliveryType = params.deliveryType
        Timber.v("Change product $productId count in the $deliveryType cart to $count")
        cartRepository.changeProductCountInCart(barcode, count, deliveryType)
    }

    data class Params(
        val productId: Product.Id,
        val barcode: Barcode,
        val count: Int,
        val deliveryType: DeliveryType,
    )
}
