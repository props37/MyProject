package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal class SignUpNavActions(
    val onBackClicked: () -> Unit,
    val onUserCreated: (PhoneNumber) -> Unit,
)
