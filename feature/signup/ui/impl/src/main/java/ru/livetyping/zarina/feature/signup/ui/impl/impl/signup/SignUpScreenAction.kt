package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

internal sealed interface SignUpScreenAction {
    data object ScreenClosed : SignUpScreenAction
}
