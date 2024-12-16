package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface OrderSideEffect : SideEffect {
    data class Navigate(val action: OrderScreenAction) : OrderSideEffect
}
