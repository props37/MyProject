package ru.zarina.zarina.usecase.old.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.favorites.IFavoritesRepository
import ru.zarina.zarina.data.old.product.IProductRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.old.Category
import ru.zarina.zarina.domain.old.FilteredProducts
import ru.zarina.zarina.domain.old.Filtration
import ru.zarina.zarina.domain.old.Page
import ru.zarina.zarina.domain.old.ProductSort
import ru.zarina.zarina.base.usecase.UseCase

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
