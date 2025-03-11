package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface ProductSideEffect : SideEffect {
    data class Navigate(val action: ProductScreenAction) : ProductSideEffect

    data class Share(val text: String) : ProductSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : ProductSideEffect
}
