package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model

import ru.livetyping.zarina.core.domain.model.category.CategoryInfo
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uicomponent.filtration.model.FiltrationState
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class FiltrationStateBuilder {
    fun build(
        filters: ProductFilters?,
        categoryInfoResult: Result<CategoryInfo>?,
        isPickupStoreFilterVisible: Boolean,
        isRefreshing: Boolean,
    ): FiltrationState {
        val categoryInfo = categoryInfoResult?.getOrNull()
        return if (filters != null) {
            val availableFilters = categoryInfo?.availableFilters
            val combinedFilters = availableFilters?.let {
                filters.coerceInAvailable(availableFilters)
            } ?: filters
            FiltrationState.Success(
                filters = combinedFilters,
                isPickupStoreFilterVisible = isPickupStoreFilterVisible,
                availableProductCount = categoryInfo?.productCount,
                isRefreshing = isRefreshing,
            )
        } else {
            categoryInfoResult?.fold(
                onSuccess = { info ->
                    FiltrationState.Success(
                        filters = info.availableFilters,
                        isPickupStoreFilterVisible = isPickupStoreFilterVisible,
                        availableProductCount = info.productCount,
                        isRefreshing = isRefreshing,
                    )
                },
                onFailure = { throwable ->
                    val errorState = ZarinaErrorScreenState.from(throwable)
                    FiltrationState.Error(errorState)
                },
            ) ?: FiltrationState.Loading
        }
    }
}
