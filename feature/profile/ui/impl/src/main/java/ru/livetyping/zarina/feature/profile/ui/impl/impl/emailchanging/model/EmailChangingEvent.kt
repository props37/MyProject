package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.model

internal sealed interface EmailChangingEvent {
    data object BackClicked : EmailChangingEvent

    data object ChangeEmailClicked : EmailChangingEvent
}
