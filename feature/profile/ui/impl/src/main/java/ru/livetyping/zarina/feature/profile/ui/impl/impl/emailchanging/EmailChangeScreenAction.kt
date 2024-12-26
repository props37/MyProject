package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging

internal sealed interface EmailChangeScreenAction {
    data object BackClicked : EmailChangeScreenAction

    data object EmailChanged : EmailChangeScreenAction
}
