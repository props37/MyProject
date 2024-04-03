package ru.zarina.zarina.ui.screen.signup

import ru.zarina.zarina.usecase.user.SignUpUseCase
import javax.inject.Inject

class SignUpInteractor @Inject constructor(
    val signUp: SignUpUseCase,
)
