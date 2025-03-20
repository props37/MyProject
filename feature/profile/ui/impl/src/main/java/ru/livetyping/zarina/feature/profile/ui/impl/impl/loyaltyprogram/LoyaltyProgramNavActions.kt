package ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class LoyaltyProgramNavActions(
    val onBackClicked: () -> Unit,
    val onBonusHistoryClicked: () -> Unit,
) : NavigationActions
