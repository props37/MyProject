package ru.livetyping.zarina.usecase.old.favorites

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.favorites.IFavoritesRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.Product

@Factory
class SetIsFavoriteUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val favoritesRepository: IFavoritesRepository,
) : UseCase<SetIsFavoriteUseCase.Params, Unit>(dispatcher) {
    override suspend fun execute(params: Params) {
        val (product, isFavorite) = params

        favoritesRepository.setIsFavorite(product, isFavorite)
    }

    data class Params(
        val product: Product,
        val isFavorite: Boolean,
    )
}
