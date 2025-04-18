package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaCartProduct
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.usecase.cart.AddProductToCartUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class AddProductToCartUseCaseImpl(
    private val cartRepository: CartRepository,
    private val appMetrica: AppMetrica,
    private val logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), AddProductToCartUseCase {

    override suspend fun execute(params: Params) {
        cartRepository.addProductToCart(
            productId = params.product.id,
            barcode = params.barcode,
            count = params.count,
        )
        appMetrica.reportProductAddedToCart(params.product.toAppMetricaCartProduct(params.count))

        if (!cartRepository.areCartProductIdsFetched()) {
            fetchCartProductIds()
        }
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private suspend fun fetchCartProductIds() {
        try {
            val cachePolicy = CachePolicy.Remote()
            cartRepository.getCartProductIdsFlow(cachePolicy).firstOrNull()
        } catch (e: Exception) {
            logger?.e(TAG, e, "Failed to fetch cart product IDs")
        }
    }

    private companion object {
        private const val TAG = "AddProductToCartUseCaseImpl"
    }
}
