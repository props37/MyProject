package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import ru.livetyping.zarina.core.domain.model.geo.City

internal class ProfileNavActions(
    val onSignInClicked: () -> Unit,
    val onChangeCityClicked: (currentCity: City?) -> Unit,
)
