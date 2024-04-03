package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class RemoveProductFromCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
    private val fetchCartProductIdsUseCase: FetchCartProductIdsUseCase,
) : UseCase<RemoveProductFromCartUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val productId = params.productId
        val barcode = params.barcode
        Timber.v("Remove product $productId from the cart")
        val cartProductCount = cartRepository.removeProductFromCart(productId, barcode)
        cartRepository.setCartTotalProductCount(cartProductCount.value)
        if (!cartRepository.areCartProductIdsFetched.value) {
            Timber.w("Cart product IDs are not fetched. Trying to fetch")
            fetchCartProductIdsUseCase()
        }
    }

    data class Params(val productId: Product.Id, val barcode: Barcode)
}
