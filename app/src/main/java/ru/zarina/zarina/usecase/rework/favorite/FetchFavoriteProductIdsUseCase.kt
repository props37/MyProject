package ru.zarina.zarina.usecase.rework.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.favorite.FavoriteRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
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
