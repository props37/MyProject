package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface ProfileScreenAction {
    data object SignInClicked : ProfileScreenAction

    data class ChangeCityClicked(val currentCity: City?) : ProfileScreenAction
}
