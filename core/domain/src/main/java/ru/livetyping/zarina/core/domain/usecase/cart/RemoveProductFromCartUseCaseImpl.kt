package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.usecase.cart.RemoveProductFromCartUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class RemoveProductFromCartUseCaseImpl(
    private val cartRepository: CartRepository,
    private val logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), RemoveProductFromCartUseCase {

    override suspend fun execute(params: Params) {
        cartRepository.removeProductFromCart(
            productId = params.productId,
            barcode = params.barcode,
        )

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
        private const val TAG = "RemoveProductFromCartUseCaseImpl"
    }
}
