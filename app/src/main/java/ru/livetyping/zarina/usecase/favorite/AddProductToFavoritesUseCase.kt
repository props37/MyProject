package ru.livetyping.zarina.usecase.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class AddProductToFavoritesUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
    private val fetchFavoriteProductIdsUseCase: FetchFavoriteProductIdsUseCase,
) : UseCase<AddProductToFavoritesUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val productId = params.productId
        Timber.v("Add product $productId to favorites")
        favoriteRepository.addProductToFavorites(productId)
        if (!favoriteRepository.areFavoriteProductIdsFetched.value) {
            Timber.w("Favorite product IDs are not fetched. Trying to fetch")
            fetchFavoriteProductIdsUseCase()
        }
    }

    data class Params(val productId: Product.Id)
}
