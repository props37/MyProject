package ru.livetyping.zarina.presentation.screen.shops

import ru.livetyping.zarina.domain.shop.Shop

sealed class ShopsScreenAction {
    data object ScreenClosed : ShopsScreenAction()

    data object LocationPermissionRequired : ShopsScreenAction()

    data class ShopClicked(val shop: Shop) : ShopsScreenAction()
}
