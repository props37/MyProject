package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class PasswordRecoveryNavActions(
    val onBackClicked: () -> Unit,
    val onPasswordResetRequested: () -> Unit,
) : NavigationActions
