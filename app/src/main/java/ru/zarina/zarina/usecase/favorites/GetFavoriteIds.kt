package ru.zarina.zarina.usecase.favorites

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.data.favorites.IFavoritesRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product

@Factory
class GetFavoriteIdsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val favoritesRepository: IFavoritesRepository,
) : FlowUseCase<Unit, Set<Product.Id>>(dispatcher) {

    override fun execute(params: Unit): Flow<Result<Set<Product.Id>>> {
        return favoritesRepository.getIds().map { Result.success(it) }
    }

}
