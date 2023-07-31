package ru.zarina.zarina.usecase.favorites

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.favorites.IFavoritesRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product
import timber.log.Timber

@Factory
class GetFavoritesPageUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val favoritesRepository: IFavoritesRepository,
) : UseCase<GetFavoritesPageUseCase.Params, Page<List<Product>>>(dispatcher) {
    override suspend fun execute(params: Params): Page<List<Product>> {
        val (pageIndex) = params

        val page = favoritesRepository.getFavorites(pageIndex)

        Timber.v("Loaded page $pageIndex, containing ${page.value.size} items")

        return page
    }

    data class Params(
        val pageIndex: Int,
    )

}
