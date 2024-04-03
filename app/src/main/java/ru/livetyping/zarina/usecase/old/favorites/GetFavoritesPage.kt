package ru.livetyping.zarina.usecase.old.favorites

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.favorites.IFavoritesRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.Product
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
