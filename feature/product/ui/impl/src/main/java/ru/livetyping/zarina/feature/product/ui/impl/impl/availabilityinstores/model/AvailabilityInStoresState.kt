package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.geo.City

@Immutable
internal data class AvailabilityInStoresState(
    val sizeState: SizeState,
    val city: City?,
    val storeListState: StoreListState,
)
