package ru.livetyping.zarina.usecase.old.favorites

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.old.favorites.IFavoritesRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.Product

@Factory
class GetFavoriteIdsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val favoritesRepository: IFavoritesRepository,
) : FlowUseCase<Unit, Set<Product.Id>>(dispatcher) {

    override fun execute(params: Unit): Flow<Set<Product.Id>> {
        return favoritesRepository.getIds()
    }
}
