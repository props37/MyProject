package ru.livetyping.zarina.feature.wishlist.ui.impl.screen

import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffect
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage2

internal sealed interface WishlistSideEffect : SideEffect {
    data class Navigate(val action: WishlistScreenAction) : WishlistSideEffect

    data class ShowZarinaToast(val message: ZarinaToastMessage2) : WishlistSideEffect
}
