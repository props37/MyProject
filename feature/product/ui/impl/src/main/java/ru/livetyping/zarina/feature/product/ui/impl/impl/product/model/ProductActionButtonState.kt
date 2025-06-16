package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

internal sealed class ProductActionButtonState {
    abstract val isLoading: Boolean

    data class AddToCart(override val isLoading: Boolean) : ProductActionButtonState()

    data class InCart(override val isLoading: Boolean) : ProductActionButtonState()

    data object NotifyWhenAvailable : ProductActionButtonState() {
        override val isLoading: Boolean get() = false
    }
}
