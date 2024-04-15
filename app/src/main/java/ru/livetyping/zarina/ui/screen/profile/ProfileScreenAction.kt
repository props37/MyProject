package ru.livetyping.zarina.ui.screen.profile

import ru.livetyping.zarina.domain.geography.City

sealed class ProfileScreenAction {
    data object SignInClicked : ProfileScreenAction()

    data object SignUpClicked : ProfileScreenAction()

    data class CityClicked(val currentCity: City?) : ProfileScreenAction()

    data object MyOrdersClicked : ProfileScreenAction()
}
