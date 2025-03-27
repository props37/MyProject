package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PickupPointSelectorSideEffect : SideEffect {
    data class Navigate(val action: PickupPointSelectorScreenAction) : PickupPointSelectorSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PickupPointSelectorSideEffect
}
