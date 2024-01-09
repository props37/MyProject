package ru.zarina.zarina.usecase.favorites

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.favorites.IFavoritesRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.base.UseCase

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
