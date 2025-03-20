package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class SignInByPhoneConfirmationNavActions(
    val onBackClicked: () -> Unit,
    val onSignInConfirmed: () -> Unit,
) : NavigationActions
