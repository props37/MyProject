package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class SignUpConfirmationNavActions(
    val onBackClicked: () -> Unit,
    val onPhoneConfirmed: () -> Unit,
) : NavigationActions
