package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface ProductSubscriptionSideEffect : SideEffect {
    data class Navigate(val action: ProductSubscriptionScreenAction) : ProductSubscriptionSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : ProductSubscriptionSideEffect
}
