package ru.livetyping.zarina.usecase.product

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductDetails
import javax.inject.Inject

class GetProductFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository,
) : FlowUseCase<GetProductFlowUseCase.Params, ProductDetails>(dispatcher) {

    override fun execute(params: Params): Flow<ProductDetails> {
        val productFlow = productRepository.getProductFlow(params.productId)
        return combine(
            productFlow,
            cartRepository.cartProductIds,
            favoriteRepository.favoriteProductIds,
        ) { product, cartProductIds, favoriteProductIds ->
            product.copy(
                isInFavorites = product.id in favoriteProductIds,
                isInCart = product.id in cartProductIds,
            )
        }
    }

    data class Params(val productId: Product.Id)
}
