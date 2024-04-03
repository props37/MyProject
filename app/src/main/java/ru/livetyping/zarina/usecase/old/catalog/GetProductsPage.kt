package ru.livetyping.zarina.usecase.old.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.favorites.IFavoritesRepository
import ru.livetyping.zarina.data.old.product.IProductRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.Category
import ru.livetyping.zarina.domain.old.FilteredProducts
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.ProductSort

@Factory
class GetProductsPageUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
    private val favoritesRepository: IFavoritesRepository,
) : UseCase<GetProductsPageUseCase.Params, Page<FilteredProducts>>(dispatcher) {
    override suspend fun execute(params: Params): Page<FilteredProducts> {
        val (category, sort, filtration, pageIndex) = params

        val page = productRepository.getProducts(category, sort, filtration, pageIndex)

        favoritesRepository.update(page.value.products)

        return page
    }

    data class Params(
        val category: Category,
        val sort: ProductSort,
        val filtration: Filtration?,
        val pageIndex: Int,
    )

}
