package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface ProfileScreenAction {
    data object BackClicked : ProfileScreenAction

    data object SignInClicked : ProfileScreenAction

    data object SignUpClicked : ProfileScreenAction

    data object ProfileDetailsClicked : ProfileScreenAction

    data object LoyaltyCardInfoClicked : ProfileScreenAction

    data object MyOrdersClicked : ProfileScreenAction

    data class ChangeCityClicked(val currentCity: City?) : ProfileScreenAction

    data object StoresClicked : ProfileScreenAction
}
