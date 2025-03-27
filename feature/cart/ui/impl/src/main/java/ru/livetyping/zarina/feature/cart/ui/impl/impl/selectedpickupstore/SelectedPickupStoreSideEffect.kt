package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface SelectedPickupStoreSideEffect : SideEffect {
    data class Navigate(val action: SelectedPickupStoreScreenAction) : SelectedPickupStoreSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : SelectedPickupStoreSideEffect
}
