package ru.livetyping.zarina.feature.profile.ui.impl.emailchange.model

internal sealed interface EmailChangeEvent {
    data object BackClicked : EmailChangeEvent

    data object ChangeEmailClicked : EmailChangeEvent
}
