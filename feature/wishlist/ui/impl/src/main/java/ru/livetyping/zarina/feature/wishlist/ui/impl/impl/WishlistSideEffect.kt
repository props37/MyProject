package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect

internal sealed interface WishlistSideEffect : SideEffect {
    data class Navigate(val action: WishlistScreenAction) : WishlistSideEffect
}
