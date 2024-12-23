package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging

internal sealed interface EmailChangingScreenAction {
    data object BackClicked : EmailChangingScreenAction
}
