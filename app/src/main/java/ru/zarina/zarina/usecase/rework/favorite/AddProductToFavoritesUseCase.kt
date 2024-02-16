package ru.zarina.zarina.usecase.rework.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.favorite.FavoriteRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.usecase.base.UseCase
import javax.inject.Inject

class AddProductToFavoritesUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
) : UseCase<AddProductToFavoritesUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        favoriteRepository.addProductToFavorites(params.productId)
    }

    data class Params(val productId: Product.Id)
}
