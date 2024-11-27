package ru.livetyping.zarina.feature.signin.ui.api

public class SignInNavActions(
    public val onUserSignedIn: () -> Unit,
    public val onSignUpClicked: () -> Unit,
)
