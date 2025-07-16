package ru.livetyping.zarina.feature.profile.ui.impl.passwordchange

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class PasswordChangeNavActions(
    val onBackClicked: () -> Unit,
    val onPasswordChanged: () -> Unit,
) : NavigationActions
