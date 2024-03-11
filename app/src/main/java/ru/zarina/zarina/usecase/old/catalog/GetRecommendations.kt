package ru.zarina.zarina.usecase.old.catalog

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.favorites.IFavoritesRepository
import ru.zarina.zarina.data.old.recommendation.IRecommendationRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.RecommendationType
import ru.zarina.zarina.base.usecase.FlowUseCase
import timber.log.Timber

@Factory
class GetRecommendationsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val recommendationRepository: IRecommendationRepository,
    private val favoritesRepository: IFavoritesRepository,
) : FlowUseCase<GetRecommendationsUseCase.Params, List<Product>>(dispatcher) {

    override fun execute(params: Params): Flow<List<Product>> {
        val (type) = params

        val recommendationsFlow = recommendationRepository.getRecommendations(type)
            .onEach {
                Timber.v("Loaded ${it.size} recommendations for type $type")
                favoritesRepository.update(it)
            }
        val favoritesFlow = favoritesRepository.getIds()

        return combine(recommendationsFlow, favoritesFlow) { recommendations, favorites ->
            recommendations.map { product ->
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
        val type: RecommendationType,
    )
}
