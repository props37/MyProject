package ru.livetyping.zarina.feature.profile.ui.impl.emailchange

import ru.livetyping.zarina.core.navigation.NavigationActions

internal class EmailChangeNavActions(
    val onBackClicked: () -> Unit,
    val onEmailChanged: () -> Unit,
) : NavigationActions
