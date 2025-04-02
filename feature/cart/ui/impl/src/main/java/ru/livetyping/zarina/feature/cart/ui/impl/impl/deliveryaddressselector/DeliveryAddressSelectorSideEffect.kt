package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface DeliveryAddressSelectorSideEffect : SideEffect {
    data class Navigate(val action: DeliveryAddressSelectorScreenAction) :
        DeliveryAddressSelectorSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : DeliveryAddressSelectorSideEffect
}
