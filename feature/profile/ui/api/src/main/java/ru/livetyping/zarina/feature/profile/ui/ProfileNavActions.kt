package ru.livetyping.zarina.feature.profile.ui

import ru.livetyping.zarina.core.domain.model.geo.City

public class ProfileNavActions(
    public val onBackClicked: () -> Unit,
    public val onSignInClicked: () -> Unit,
    public val onSignUpClicked: () -> Unit,
    public val onChangeCityClicked: (currentCity: City?) -> Unit,
)
