package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCartProductIdsFlowUseCaseImpl(
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Set<Product.Id>>(logger), GetCartProductIdsFlowUseCase {

    override fun execute(params: Params): Flow<Set<Product.Id>> {
        return cartRepository.getCartProductIdsFlow(params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<Set<Product.Id>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCartProductIdsFlowUseCaseImpl"
    }
}
