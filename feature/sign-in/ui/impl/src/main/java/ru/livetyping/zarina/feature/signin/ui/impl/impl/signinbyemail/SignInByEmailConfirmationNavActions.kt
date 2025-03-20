package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class SignInByEmailConfirmationNavActions(
    val onBackClicked: () -> Unit,
    val onSignInConfirmed: () -> Unit,
) : NavigationActions
