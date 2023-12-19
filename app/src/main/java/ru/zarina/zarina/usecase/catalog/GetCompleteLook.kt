package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.data.favorites.FavoritesRepository
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product
import timber.log.Timber

@Factory
class GetCompleteLookUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
    private val favoritesRepository: FavoritesRepository,
) : FlowUseCase<GetCompleteLookUseCase.Params, List<Product>>(dispatcher) {

    override fun execute(params: Params): Flow<List<Product>> {
        val (product) = params

        if (!product.isLookPart) return flowOf(emptyList())

        val completeLookFlow = productRepository.getCompleteLook(product)
            .onEach {
                Timber.v("Complete look for $product contains ${it.size} items")
                favoritesRepository.update(it)
            }

        val favoritesFlow = favoritesRepository.getIds()

        return combine(completeLookFlow, favoritesFlow) { look, favorites ->
            look.map { product ->
                val isFavorite = product.id in favorites
                if (product.isFavorite == isFavorite) {
                    product
                } else {
                    product.copy(isFavorite = isFavorite)
                }
            }
        }
    }

    data class Params(
        val product: Product,
    )
}
