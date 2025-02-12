package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage

internal sealed interface ProductListSideEffect : SideEffect {
    data class Navigate(val action: ProductListScreenAction) : ProductListSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage) : ProductListSideEffect
}
