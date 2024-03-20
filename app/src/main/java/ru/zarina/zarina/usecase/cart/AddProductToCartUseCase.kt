package ru.zarina.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.cart.CartRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
    private val fetchCartProductIdsUseCase: FetchCartProductIdsUseCase,
) : UseCase<AddProductToCartUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val productId = params.productId
        val barcode = params.barcode
        val count = params.count
        val result = cartRepository.addProductToCart(productId, barcode, count)
        cartRepository.setCartTotalProductCount(result.cartProductCount)
        if (!cartRepository.areCartProductIdsFetched.value) {
            Timber.w("Cart product IDs are not fetched. Trying to fetch")
            fetchCartProductIdsUseCase()
        }
    }

    data class Params(val productId: Product.Id, val barcode: Barcode, val count: Int)
}
