package ru.livetyping.zarina.usecase.favorite

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class GetFavoriteProductIdsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
    private val fetchFavoriteProductIdsUseCase: FetchFavoriteProductIdsUseCase,
) : FlowUseCase<Unit, Set<Product.Id>>(dispatcher) {

    override fun execute(params: Unit): Flow<Set<Product.Id>> {
        return combineTransform(
            favoriteRepository.favoriteProductIds,
            favoriteRepository.areFavoriteProductIdsFetched,
        ) { favoriteProductIds, areFavoriteProductIdsFetched ->
            emit(favoriteProductIds)
            if (!areFavoriteProductIdsFetched) {
                Timber.w("Favorite product IDs are not fetched. Trying to fetch")
                fetchFavoriteProductIdsUseCase()
            }
        }
    }
}
