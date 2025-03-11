package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.geo.City

@Immutable
internal data class AvailabilityInStoresState(
    val sizes: ImmutableList<Size>,
    val city: City?,
)
