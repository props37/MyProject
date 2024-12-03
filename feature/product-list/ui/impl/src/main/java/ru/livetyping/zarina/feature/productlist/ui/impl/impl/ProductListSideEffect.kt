package ru.livetyping.zarina.feature.productlist.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface ProductListSideEffect : SideEffect {
    data class Navigate(val action: ProductListScreenAction) : ProductListSideEffect
}
