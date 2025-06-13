package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

@Immutable
internal data class SizeSelectorItem(
    val offer: ProductOffer,
    val isSelected: Boolean,
)
