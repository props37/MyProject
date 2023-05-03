package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.data.recommendation.IRecommendationRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType
import ru.zarina.zarina.utils.coroutine.mapAsync
import timber.log.Timber
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val recommendationRepository: IRecommendationRepository,
    private val productRepository: IProductRepository,
) : UseCase<GetRecommendationsUseCase.Params, List<Product>>(dispatcher) {
    override suspend fun execute(params: Params): List<Product> {
        val (type) = params

        val recommendationIds = recommendationRepository.getRecommendations(type)

        val recommendations = recommendationIds
            .mapAsync { id ->
                runCatching { productRepository.getProduct(id) }
                    .onFailure { Timber.w(it, "Product with id $id failed to load") }
                    .getOrNull()
            }
            .filterNotNull()

        Timber.v("Loaded ${recommendations.size} recommendations for type $type")

        return recommendations
    }

    data class Params(
        val type: RecommendationType,
    )

}
