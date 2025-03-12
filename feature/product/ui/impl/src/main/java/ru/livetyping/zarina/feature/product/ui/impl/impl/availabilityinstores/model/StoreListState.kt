package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class StoreListState {

    @Immutable
    data class Success(
        val availabilityList: ImmutableList<ProductAvailabilityInStore>,
    ) : StoreListState()

    data object Loading : StoreListState()

    data object Empty : StoreListState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : StoreListState()
}
