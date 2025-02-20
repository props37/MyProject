package ru.livetyping.zarina.core.uicomponent.filtration.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
public sealed class ProductFiltrationState {
    @Immutable
    public data class Success(
        val filters: ProductFilters,
        val isPickupStoreFilterVisible: Boolean,
        val availableProductCount: Int?,
        val isRefreshing: Boolean,
    ) : ProductFiltrationState()

    public data object Loading : ProductFiltrationState()

    @Immutable
    public data class Error(val errorState: ZarinaErrorScreenState) : ProductFiltrationState()
}
