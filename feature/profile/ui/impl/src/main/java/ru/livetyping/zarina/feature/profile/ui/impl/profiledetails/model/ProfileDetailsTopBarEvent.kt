package ru.livetyping.zarina.feature.profile.ui.impl.profiledetails.model

internal sealed interface ProfileDetailsTopBarEvent {
    data object BackClicked : ProfileDetailsTopBarEvent

    data object SaveChangesClicked : ProfileDetailsTopBarEvent
}
