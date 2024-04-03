package ru.livetyping.zarina.ui.screens.product

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.domain.old.RecommendationType
import ru.livetyping.zarina.usecase.old.catalog.GetCompleteLookUseCase
import ru.livetyping.zarina.usecase.old.catalog.GetDeliveryAvailabilityUseCase
import ru.livetyping.zarina.usecase.old.catalog.GetProductUseCase
import ru.livetyping.zarina.usecase.old.catalog.GetRecommendationsUseCase
import ru.livetyping.zarina.usecase.old.favorites.SetIsFavoriteUseCase

@Factory
class ProductInteractor(
    private val getProductUseCase: GetProductUseCase,
    private val getCompleteLookUseCase: GetCompleteLookUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val getDeliveryAvailabilityUseCase: GetDeliveryAvailabilityUseCase,
    private val setIsFavoriteUseCase: SetIsFavoriteUseCase,
) {
    fun getProduct(id: Product.Id) = getProductUseCase(GetProductUseCase.Params(id))

    fun getCompleteLook(product: Product) =
        getCompleteLookUseCase(GetCompleteLookUseCase.Params(product))

    fun getRecommendations(product: Product) =
        getRecommendationsUseCase(
            GetRecommendationsUseCase.Params(
                RecommendationType.Similar(
                    product
                )
            )
        )

    suspend fun getDeliveryAvailability(product: Product) =
        getDeliveryAvailabilityUseCase(GetDeliveryAvailabilityUseCase.Params(product))

    suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        setIsFavoriteUseCase(SetIsFavoriteUseCase.Params(product, isFavorite))
}
