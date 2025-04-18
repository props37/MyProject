package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface OrderConfirmedSideEffect : SideEffect {
    data class Navigate(val action: OrderConfirmedScreenAction) : OrderConfirmedSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : OrderConfirmedSideEffect
}
