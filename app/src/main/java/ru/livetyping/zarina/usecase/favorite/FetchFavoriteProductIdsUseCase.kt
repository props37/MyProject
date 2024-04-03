package ru.livetyping.zarina.usecase.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.product.Product
import timber.log.Timber
import javax.inject.Inject

class FetchFavoriteProductIdsUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
) : UseCase<Unit, Set<Product.Id>>(dispatcher) {

    override suspend fun execute(params: Unit): Set<Product.Id> {
        Timber.v("Fetch favorite product IDs")
        return favoriteRepository.fetchFavoriteProductIds()
    }
}
