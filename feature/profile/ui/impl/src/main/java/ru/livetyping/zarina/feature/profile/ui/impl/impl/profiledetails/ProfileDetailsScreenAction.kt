package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

internal sealed interface ProfileDetailsScreenAction {
    data object ScreenClosed : ProfileDetailsScreenAction
}
