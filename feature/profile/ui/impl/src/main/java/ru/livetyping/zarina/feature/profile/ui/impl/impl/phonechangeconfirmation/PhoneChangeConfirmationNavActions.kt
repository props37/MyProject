package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class PhoneChangeConfirmationNavActions(
    val onBackClicked: () -> Unit,
    val onPhoneChangeConfirmed: () -> Unit,
) : NavigationActions
