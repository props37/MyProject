package ru.zarina.zarina.usecase.content

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.data.content.IContentRepository
import ru.zarina.zarina.data.favorites.IFavoritesRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Selection
import timber.log.Timber

@Factory
class GetSelectionsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val contentRepository: IContentRepository,
    private val favoritesRepository: IFavoritesRepository,
) : FlowUseCase<Unit, List<Selection>>(dispatcher) {

    override fun execute(params: Unit): Flow<Result<List<Selection>>> {
        val selectionsFlow = contentRepository.getSelections()
            .onEach { selections ->
                Timber.v("Loaded ${selections.size} selections")
                favoritesRepository.update(selections.flatMap { (it as? Selection.Products)?.products.orEmpty() })
            }
        val favoritesFlow = favoritesRepository.getIds()
        return combine(selectionsFlow, favoritesFlow) { selections, favorites ->
            val result = selections.map { selection ->
                if (selection !is Selection.Products) {
                    selection
                } else {
                    val newProducts = selection.products
                        .map { product ->
                            val isFavorite = product.id in favorites
                            if (product.isFavorite == isFavorite) {
                                product
                            } else {
                                product.copy(isFavorite = isFavorite)
                            }
                        }
                        .toPersistentList()
                    selection.copy(products = newProducts)
                }
            }
            Result.success(result)
        }
    }
}
