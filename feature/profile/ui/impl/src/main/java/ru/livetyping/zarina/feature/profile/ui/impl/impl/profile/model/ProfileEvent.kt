package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model

internal sealed interface ProfileEvent {
    data object ProfileDetailsClicked : ProfileEvent

    data object SignInClicked : ProfileEvent

    data object SignUpClicked : ProfileEvent

    data object LoyaltyCardInfoClicked : ProfileEvent

    data class MenuItemClicked(val item: ProfileMenuItem) : ProfileEvent
}
