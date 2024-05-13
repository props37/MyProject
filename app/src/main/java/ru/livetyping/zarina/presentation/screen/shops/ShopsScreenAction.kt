package ru.livetyping.zarina.presentation.screen.shops

sealed class ShopsScreenAction {
    data object ScreenClosed : ShopsScreenAction()

    data object LocationPermissionRequired : ShopsScreenAction()
}
