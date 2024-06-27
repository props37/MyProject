package ru.livetyping.zarina.presentation.screen.profile

import ru.livetyping.zarina.domain.geography.City

sealed class ProfileScreenAction {
    data object ProfileDetailsClicked : ProfileScreenAction()

    data object SignInClicked : ProfileScreenAction()

    data object SignUpClicked : ProfileScreenAction()

    data object LoyaltyCardInfoClicked : ProfileScreenAction()

    data class CityClicked(val currentCity: City?) : ProfileScreenAction()

    data object MyOrdersClicked : ProfileScreenAction()

    data object StoresClicked : ProfileScreenAction()
}
