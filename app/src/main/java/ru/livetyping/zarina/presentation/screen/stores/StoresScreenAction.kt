package ru.livetyping.zarina.presentation.screen.stores

import ru.livetyping.zarina.domain.store.Store

sealed class StoresScreenAction {
    data object ScreenClosed : StoresScreenAction()

    data object LocationPermissionRequired : StoresScreenAction()

    data class StoreClicked(val store: Store) : StoresScreenAction()
}
