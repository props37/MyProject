package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model

internal sealed interface ProfileDetailsEvent {
    data class BirthDateEpochMillisChanged(val millis: Long?) : ProfileDetailsEvent

    data object ErrorRefreshClicked : ProfileDetailsEvent
}
