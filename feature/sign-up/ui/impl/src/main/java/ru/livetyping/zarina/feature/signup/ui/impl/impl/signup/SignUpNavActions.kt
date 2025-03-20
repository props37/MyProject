package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class SignUpNavActions(
    val onBackClicked: () -> Unit,
    val onUserCreated: (PhoneNumber) -> Unit,
) : NavigationActions
