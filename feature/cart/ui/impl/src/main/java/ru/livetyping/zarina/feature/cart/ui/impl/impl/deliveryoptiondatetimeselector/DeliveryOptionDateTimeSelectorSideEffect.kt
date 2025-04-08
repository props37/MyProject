package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface DeliveryOptionDateTimeSelectorSideEffect : SideEffect {
    data class Navigate(val action: DeliveryOptionDateTimeSelectorScreenAction) :
        DeliveryOptionDateTimeSelectorSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) :
        DeliveryOptionDateTimeSelectorSideEffect
}
