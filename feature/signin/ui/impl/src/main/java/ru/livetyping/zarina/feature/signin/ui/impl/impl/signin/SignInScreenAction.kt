package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

internal sealed interface SignInScreenAction {
    data object ScreenClosed : SignInScreenAction
}
