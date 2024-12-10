package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface ProductSubscriptionSideEffect : SideEffect {
    data class Navigate(val action: ProductSubscriptionScreenAction) : ProductSubscriptionSideEffect
}
