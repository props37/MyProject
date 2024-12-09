package ru.livetyping.zarina.core.uikit.sizeselector

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

@Immutable
internal data class SizeSelectorSizeItem(
    val size: String,
    val offers: List<ProductOffer>,
) {
    val id: String get() = size

    val isAvailable: Boolean by lazy {
        offers.any { it.isAvailable }
    }

    fun getAvailableHeights(): List<String> {
        return offers
            .filter { it.isAvailable }
            .mapNotNull { it.height }
            .sorted()
    }
}
