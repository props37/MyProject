package ru.zarina.zarina.ui.screen.profile

import ru.zarina.zarina.domain.geography.City

sealed class ProfileScreenAction {
    data class CityClicked(val currentCity: City?) : ProfileScreenAction()
}
