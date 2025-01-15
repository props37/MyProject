package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.cart.CartProduct

@Stable
internal class ProductCardActions(
    val onProductClicked: (CartProduct) -> Unit,
    val onCountClicked: (CartProduct) -> Unit,
    val onAddToFavoritesClicked: (CartProduct) -> Unit,
    val onDeleteFromCartClicked: (CartProduct) -> Unit,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductCardActions

        if (onCountClicked != other.onCountClicked) return false
        if (onAddToFavoritesClicked != other.onAddToFavoritesClicked) return false
        if (onDeleteFromCartClicked != other.onDeleteFromCartClicked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = onCountClicked.hashCode()
        result = 31 * result + onAddToFavoritesClicked.hashCode()
        result = 31 * result + onDeleteFromCartClicked.hashCode()
        return result
    }
}
