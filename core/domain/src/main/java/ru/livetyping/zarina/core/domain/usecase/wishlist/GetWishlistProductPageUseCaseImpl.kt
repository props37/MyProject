package ru.livetyping.zarina.core.domain.usecase.wishlist

import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductPageUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetWishlistProductPageUseCaseImpl(
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Page<List<ProductShort>>>(logger), GetWishlistProductPageUseCase {

    override suspend fun execute(params: Params): Page<List<ProductShort>> {
        return wishlistRepository.getWishlistProductPage(params.page)
    }

    override suspend fun invoke(params: Params): Result<Page<List<ProductShort>>> {
        return call(params)
    }
}
