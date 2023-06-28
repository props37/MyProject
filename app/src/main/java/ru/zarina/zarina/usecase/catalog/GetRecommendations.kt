package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.recommendation.IRecommendationRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType
import timber.log.Timber

@Factory
class GetRecommendationsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val recommendationRepository: IRecommendationRepository,
) : UseCase<GetRecommendationsUseCase.Params, List<Product>>(dispatcher) {
    override suspend fun execute(params: Params): List<Product> {
        val (type) = params

        val recommendations = recommendationRepository.getRecommendations(type)

        Timber.v("Loaded ${recommendations.size} recommendations for type $type")

        return recommendations
    }

    data class Params(
        val type: RecommendationType,
    )

}
