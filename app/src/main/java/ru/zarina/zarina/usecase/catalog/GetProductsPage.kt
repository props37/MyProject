package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.favorites.IFavoritesRepository
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.ProductSort

@Factory
class GetProductsPageUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
    private val favoritesRepository: IFavoritesRepository,
) : UseCase<GetProductsPageUseCase.Params, Page<FilteredProducts>>(dispatcher) {
    override suspend fun execute(params: Params): Page<FilteredProducts> {
        val (category, sort, filtration, pageIndex) = params

        val page = productRepository.getProducts(category, sort, filtration, pageIndex)
        val favorites = favoritesRepository.getIds().first()

        val result =
            page.copy(value = page.value.copy(products = page.value.products.map { product ->
                val isFavorite = favorites.contains(product.id)
                if (isFavorite == product.isFavorite)
                    product
                else
                    product.copy(isFavorite = isFavorite)
            }))

        return result
    }

    data class Params(
        val category: Category,
        val sort: ProductSort,
        val filtration: Filtration?,
        val pageIndex: Int,
    )

}
