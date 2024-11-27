package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model

internal sealed interface SignUpEvent {
    data object BackClicked : SignUpEvent
}
