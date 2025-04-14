package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface OrderPlacingSideEffect : SideEffect {
    data class Navigate(val action: OrderPlacingScreenAction) : OrderPlacingSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : OrderPlacingSideEffect

    data object HideKeyboard : OrderPlacingSideEffect

    data class OpenUrl(val url: Url) : OrderPlacingSideEffect
}
