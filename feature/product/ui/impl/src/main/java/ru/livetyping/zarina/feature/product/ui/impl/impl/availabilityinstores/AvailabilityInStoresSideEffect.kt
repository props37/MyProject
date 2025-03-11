package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface AvailabilityInStoresSideEffect : SideEffect {
    data class Navigate(val action: AvailabilityInStoresScreenAction) :
        AvailabilityInStoresSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : AvailabilityInStoresSideEffect
}
