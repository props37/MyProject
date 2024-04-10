package ru.livetyping.zarina.usecase.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.product.Product
import timber.log.Timber
import javax.inject.Inject

class ToggleProductPresenceInFavoritesUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
    private val addProductToFavoritesUseCase: AddProductToFavoritesUseCase,
    private val removeProductFromFavoritesUseCase: RemoveProductFromFavoritesUseCase,
) : UseCase<ToggleProductPresenceInFavoritesUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val productId = params.productId
        Timber.v("Toggle presence of product $productId in favorites")
        val favoriteProductIds = favoriteRepository.favoriteProductIds.value
        if (productId in favoriteProductIds) {
            val removeParams = RemoveProductFromFavoritesUseCase.Params(productId)
            removeProductFromFavoritesUseCase(removeParams).getOrThrow()
        } else {
            val addParams = AddProductToFavoritesUseCase.Params(productId)
            addProductToFavoritesUseCase(addParams).getOrThrow()
        }
    }

    data class Params(val productId: Product.Id)
}
