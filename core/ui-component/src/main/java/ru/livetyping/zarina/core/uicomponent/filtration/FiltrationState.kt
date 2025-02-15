package ru.livetyping.zarina.core.uicomponent.filtration

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
public sealed class FiltrationState {
    @Immutable
    public data class Success(
        val filters: ProductFilters,
        val isPickupStoreFilterVisible: Boolean,
        val availableProductCount: Int?,
        val isRefreshing: Boolean,
    ) : FiltrationState()

    public data object Loading : FiltrationState()

    @Immutable
    public data class Error(val errorState: ZarinaErrorScreenState) : FiltrationState()
}
