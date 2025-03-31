package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SelectedPickupPointSideEffect : SideEffect {
    data class Navigate(val action: SelectedPickupPointScreenAction) : SelectedPickupPointSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : SelectedPickupPointSideEffect
}
