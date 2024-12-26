package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.model

internal sealed interface EmailChangeEvent {
    data object BackClicked : EmailChangeEvent

    data object ChangeEmailClicked : EmailChangeEvent
}
