package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface OrderSideEffect : SideEffect {
    data class Navigate(val action: OrderScreenAction) : OrderSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : OrderSideEffect
}
