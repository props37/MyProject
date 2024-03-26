package ru.zarina.zarina.usecase.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.favorite.FavoriteRepository
import ru.zarina.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class ClearFavoriteProductsUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Clear favorite products")
        favoriteRepository.clearFavoriteProducts()
    }
}
