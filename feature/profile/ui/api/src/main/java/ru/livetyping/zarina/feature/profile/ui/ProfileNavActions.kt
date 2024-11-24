package ru.livetyping.zarina.feature.profile.ui

import ru.livetyping.zarina.core.domain.model.geo.City

public class ProfileNavActions(
    public val onSignInClicked: () -> Unit,
    public val onChangeCityClicked: (currentCity: City?) -> Unit,
)
