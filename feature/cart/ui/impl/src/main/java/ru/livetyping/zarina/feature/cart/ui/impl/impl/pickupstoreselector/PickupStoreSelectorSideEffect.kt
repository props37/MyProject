package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface PickupStoreSelectorSideEffect : SideEffect {
    data class Navigate(val action: PickupStoreSelectorScreenAction) : PickupStoreSelectorSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : PickupStoreSelectorSideEffect
}
