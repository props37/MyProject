package ru.zarina.zarina.ui.screens.product

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType
import ru.zarina.zarina.usecase.catalog.GetCompleteLookUseCase
import ru.zarina.zarina.usecase.catalog.GetDeliveryAvailabilityUseCase
import ru.zarina.zarina.usecase.catalog.GetProductUseCase
import ru.zarina.zarina.usecase.catalog.GetRecommendationsUseCase

@Factory
class ProductInteractor(
    private val getProductUseCase: GetProductUseCase,
    private val getCompleteLookUseCase: GetCompleteLookUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val getDeliveryAvailabilityUseCase: GetDeliveryAvailabilityUseCase,
) {
    suspend fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

    suspend fun getCompleteLook(product: Product) =
        getCompleteLookUseCase(GetCompleteLookUseCase.Params(product))

    suspend fun getRecommendations(product: Product) =
        getRecommendationsUseCase(
            GetRecommendationsUseCase.Params(
                RecommendationType.Similar(
                    product
                )
            )
        )

    suspend fun getDeliveryAvailability(product: Product) =
        getDeliveryAvailabilityUseCase(GetDeliveryAvailabilityUseCase.Params(product))
}
