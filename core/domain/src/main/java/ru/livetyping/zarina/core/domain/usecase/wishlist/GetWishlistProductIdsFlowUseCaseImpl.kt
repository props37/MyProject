package ru.livetyping.zarina.core.domain.usecase.wishlist

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetWishlistProductIdsFlowUseCaseImpl(
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Set<Product.Id>>(logger), GetWishlistProductIdsFlowUseCase {

    override fun execute(params: Unit): Flow<Set<Product.Id>> {
        return wishlistRepository.getWishlistProductIdsFlow()
    }

    override fun invoke(): Flow<Result<Set<Product.Id>>> {
        return call(Unit)
    }
}
