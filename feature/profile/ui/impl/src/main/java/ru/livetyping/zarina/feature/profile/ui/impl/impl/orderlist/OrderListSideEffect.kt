package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface OrderListSideEffect : SideEffect {
    data class Navigate(val action: OrderListScreenAction) : OrderListSideEffect
}
