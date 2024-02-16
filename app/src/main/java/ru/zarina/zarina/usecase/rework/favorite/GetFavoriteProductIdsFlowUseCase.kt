package ru.zarina.zarina.usecase.rework.favorite

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.favorite.FavoriteRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.usecase.base.FlowUseCase
import javax.inject.Inject

class GetFavoriteProductIdsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
) : FlowUseCase<Unit, Set<Product.Id>>(dispatcher) {

    override fun execute(params: Unit): Flow<Set<Product.Id>> {
        return favoriteRepository.getFavoriteProductIds()
    }
}
