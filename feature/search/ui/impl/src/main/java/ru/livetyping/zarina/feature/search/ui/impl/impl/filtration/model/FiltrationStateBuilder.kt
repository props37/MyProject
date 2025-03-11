package ru.livetyping.zarina.feature.search.ui.impl.impl.filtration.model

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.search.SearchResult
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationState
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class FiltrationStateBuilder {

    @Suppress("NAME_SHADOWING")
    fun build(
        filters: ProductFilters?,
        searchResult: Result<SearchResult>?,
        isPickupStoreFilterVisible: Boolean,
        isRefreshing: Boolean,
    ): ProductFiltrationState {
        val searchResultValue = searchResult?.getOrNull()
        return if (filters != null) {
            val availableFilters = searchResultValue?.availableFilters
            val combinedFilters = availableFilters?.let {
                filters.coerceInAvailable(availableFilters)
            } ?: filters
            ProductFiltrationState.Success(
                filters = combinedFilters,
                isPickupStoreFilterVisible = isPickupStoreFilterVisible,
                availableProductCount = searchResultValue?.productTotalCount,
                isRefreshing = isRefreshing,
            )
        } else {
            searchResult?.fold(
                onSuccess = { searchResult ->
                    ProductFiltrationState.Success(
                        filters = searchResult.availableFilters,
                        isPickupStoreFilterVisible = isPickupStoreFilterVisible,
                        availableProductCount = searchResult.productTotalCount,
                        isRefreshing = isRefreshing,
                    )
                },
                onFailure = { throwable ->
                    val errorState = ZarinaErrorScreenState.from(throwable)
                    ProductFiltrationState.Error(errorState)
                },
            ) ?: ProductFiltrationState.Loading
        }
    }
}