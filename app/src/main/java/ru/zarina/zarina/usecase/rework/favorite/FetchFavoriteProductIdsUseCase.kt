package ru.zarina.zarina.usecase.rework.favorite

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.favorite.FavoriteRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.usecase.base.UseCase
import javax.inject.Inject

class FetchFavoriteProductIdsUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        favoriteRepository.fetchFavoriteProductIds()
    }
}
