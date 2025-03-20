package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface DeliveryMethodSelectorSideEffect : SideEffect {
    data class Navigate(val action: DeliveryMethodSelectorScreenAction) :
        DeliveryMethodSelectorSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : DeliveryMethodSelectorSideEffect
}
