package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model

internal sealed interface CartListSideEffect {
    data object ScrollToProductLimitExceededError : CartListSideEffect
}
