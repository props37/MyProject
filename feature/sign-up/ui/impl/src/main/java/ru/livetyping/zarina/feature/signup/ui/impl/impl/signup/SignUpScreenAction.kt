package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal sealed interface SignUpScreenAction {
    data object BackClicked : SignUpScreenAction

    data class UserCreated(val phone: PhoneNumber) : SignUpScreenAction
}
