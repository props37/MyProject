package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
    private val fetchCartProductIdsUseCase: FetchCartProductIdsUseCase,
) : UseCase<AddProductToCartUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val product = params.product
        val barcode = params.barcode
        val count = params.count
        Timber.v("Add product $product to the cart")
        val cartProductCount = cartRepository.addProductToCart(product.id, barcode, count)
        AppMetricaHelper.reportProductAddedToCart(product, count)
        cartRepository.setCartTotalProductCount(cartProductCount.value)

        if (!cartRepository.areCartProductIdsFetched.value) {
            Timber.w("Cart product IDs are not fetched. Trying to fetch")
            fetchCartProductIdsUseCase()
        }
    }

    data class Params(val product: Product, val barcode: Barcode, val count: Int)
}
