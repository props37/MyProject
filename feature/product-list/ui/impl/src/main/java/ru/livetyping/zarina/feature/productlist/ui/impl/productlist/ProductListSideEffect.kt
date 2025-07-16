package ru.livetyping.zarina.feature.productlist.ui.impl.productlist

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2

internal sealed interface ProductListSideEffect : SideEffect {
    data class Navigate(val action: ProductListScreenAction) : ProductListSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage2) : ProductListSideEffect
}
