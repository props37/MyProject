package ru.zarina.zarina.usecase.favorites

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.favorites.IFavoritesRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.base.usecase.FlowUseCase

@Factory
class GetFavoriteIdsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val favoritesRepository: IFavoritesRepository,
) : FlowUseCase<Unit, Set<Product.Id>>(dispatcher) {

    override fun execute(params: Unit): Flow<Set<Product.Id>> {
        return favoritesRepository.getIds()
    }
}
