package ru.livetyping.zarina.feature.product.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface ProductSideEffect : SideEffect {
    data class Navigate(val action: ProductScreenAction) : ProductSideEffect
}
