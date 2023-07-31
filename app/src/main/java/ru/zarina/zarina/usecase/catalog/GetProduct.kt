package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.data.favorites.IFavoritesRepository
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product

@Factory
class GetProductUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
    private val favoritesRepository: IFavoritesRepository,
) : FlowUseCase<GetProductUseCase.Params, Product>(dispatcher) {

    override fun execute(params: Params): Flow<Result<Product>> {
        val (id) = params

        val productFlow = productRepository.getProduct(id)
            .onEach {
                favoritesRepository.update(listOf(it))
            }

        val isFavoriteFlow = favoritesRepository.getIds()
            .map { id in it }

        return combine(productFlow, isFavoriteFlow) { product, isFavorite ->
            val resultProduct = if (product.isFavorite == isFavorite)
                product
            else
                product.copy(isFavorite = isFavorite)
            Result.success(resultProduct)
        }
    }

    data class Params(
        val id: Product.Id,
    )

}
