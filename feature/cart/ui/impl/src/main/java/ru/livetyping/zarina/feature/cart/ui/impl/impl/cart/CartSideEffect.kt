package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface CartSideEffect : SideEffect {
    data class Navigate(val action: CartScreenAction) : CartSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : CartSideEffect

    data object HideKeyboard : CartSideEffect
}
