package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import ru.livetyping.zarina.core.domain.model.geo.City

internal class ProfileNavActions(
    val onBackClicked: () -> Unit,
    val onSignInClicked: () -> Unit,
    val onSignUpClicked: () -> Unit,
    val onProfileDetailsClicked: () -> Unit,
    val onLoyaltyCardInfoClicked: () -> Unit,
    val onMyOrdersClicked: () -> Unit,
    val onChangeCityClicked: (currentCity: City?) -> Unit,
)
