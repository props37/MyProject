package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class SignInNavActions(
    val onBackClicked: () -> Unit,
    val onUserSignedIn: () -> Unit,
    val onSignInByPhoneRequested: (PhoneNumber) -> Unit,
    val onForgotPasswordClicked: () -> Unit,
    val onSignUpClicked: () -> Unit,
    val onSignInByEmailPhoneConfirmationNeeded: (PhoneNumber) -> Unit,
) : NavigationActions
