package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class FiltrationState {
    @Immutable
    data class Success(
        val filters: ProductFilters,
        val isPickupStoreFilterVisible: Boolean,
        val availableProductCount: Int?,
        val isRefreshing: Boolean,
    ) : FiltrationState()

    data object Loading : FiltrationState()

    @Immutable
    data class Error(val errorState: ZarinaErrorScreenState) : FiltrationState()
}
