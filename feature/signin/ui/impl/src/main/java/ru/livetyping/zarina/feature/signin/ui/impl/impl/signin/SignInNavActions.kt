package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal class SignInNavActions(
    val onBackClicked: () -> Unit,
    val onUserSignedIn: () -> Unit,
    val onSignInByPhoneRequested: (PhoneNumber) -> Unit,
)
