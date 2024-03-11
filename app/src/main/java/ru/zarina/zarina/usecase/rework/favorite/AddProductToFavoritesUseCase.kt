package ru.zarina.zarina.usecase.rework.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.favorite.FavoriteRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class AddProductToFavoritesUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
    private val fetchFavoriteProductIdsUseCase: FetchFavoriteProductIdsUseCase,
) : UseCase<AddProductToFavoritesUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        favoriteRepository.addProductToFavorites(params.productId)
        if (!favoriteRepository.areFavoriteProductIdsFetched.value) {
            Timber.w("Favorite product IDs are not fetched. Trying to fetch")
            fetchFavoriteProductIdsUseCase()
        }
    }

    data class Params(val productId: Product.Id)
}
