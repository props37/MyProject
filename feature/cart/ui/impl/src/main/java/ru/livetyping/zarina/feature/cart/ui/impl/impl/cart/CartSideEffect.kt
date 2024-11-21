package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface CartSideEffect : SideEffect {
    data class Navigate(val action: CartScreenAction) : CartSideEffect
}
