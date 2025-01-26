package ru.livetyping.zarina.feature.signin.ui.api

import ru.livetyping.zarina.core.navigation.NavigationActions

public class SignInNavActions(
    public val onSignUpClicked: () -> Unit,
) : NavigationActions
