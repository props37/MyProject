package ru.livetyping.zarina.core.domain.usecase.wishlist

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductPageFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetWishlistProductPageFlowUseCaseImpl(
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Page<List<ProductShort>>>(logger), GetWishlistProductPageFlowUseCase {

    override fun execute(params: Params): Flow<Page<List<ProductShort>>> {
        return wishlistRepository.getWishlistProductPageFlow(params.page)
    }

    override fun invoke(params: Params): Flow<Result<Page<List<ProductShort>>>> {
        return call(params)
    }
}
