package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
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
        val cartType = params.cartType
        Timber.v("Change product $productId count in the $cartType cart to $count")
        cartRepository.changeProductCountInCart(barcode, count, cartType)
    }

    data class Params(
        val productId: Product.Id,
        val barcode: Barcode,
        val count: Int,
        val cartType: CartType,
    )
}
