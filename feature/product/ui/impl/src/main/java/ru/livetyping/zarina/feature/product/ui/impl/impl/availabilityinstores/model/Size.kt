package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

@Immutable
internal data class Size(
    val offer: ProductOffer,
    val isHeightVisible: Boolean,
    val isSelected: Boolean,
)
