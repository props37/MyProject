package ru.livetyping.zarina.usecase.favorite

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.domain.product.Product
import javax.inject.Inject

class GetFavoriteProductIdsFlowUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) : FlowUseCase<Unit, Set<Product.Id>>() {

    override fun execute(params: Unit): Flow<Set<Product.Id>> {
        return favoriteRepository.favoriteProductIds
    }
}
