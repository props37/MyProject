package ru.livetyping.zarina.feature.profile.ui.impl.profile.model

internal sealed interface ProfileEvent {
    data object ProfileDetailsClicked :
        ru.livetyping.zarina.feature.profile.ui.impl.profile.model.ProfileEvent

    data object SignInClicked :
        ru.livetyping.zarina.feature.profile.ui.impl.profile.model.ProfileEvent

    data object SignUpClicked :
        ru.livetyping.zarina.feature.profile.ui.impl.profile.model.ProfileEvent

    data object LoyaltyCardInfoClicked :
        ru.livetyping.zarina.feature.profile.ui.impl.profile.model.ProfileEvent

    data class MenuItemClicked(val item: ru.livetyping.zarina.feature.profile.ui.impl.profile.model.ProfileMenuItem) :
        ru.livetyping.zarina.feature.profile.ui.impl.profile.model.ProfileEvent

    data object LeaveFeedbackClicked :
        ru.livetyping.zarina.feature.profile.ui.impl.profile.model.ProfileEvent
}
