package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging

internal sealed interface PhoneChangingScreenAction {
    data object BackClicked : PhoneChangingScreenAction
}
