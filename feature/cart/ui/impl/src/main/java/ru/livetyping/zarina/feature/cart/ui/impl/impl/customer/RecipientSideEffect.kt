package ru.livetyping.zarina.feature.cart.ui.impl.impl.customer

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface RecipientSideEffect : SideEffect {
    data class Navigate(val action: RecipientScreenAction) : RecipientSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : RecipientSideEffect
}
