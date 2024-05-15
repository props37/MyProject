package ru.livetyping.zarina.presentation.screen.signup

import ru.livetyping.zarina.usecase.user.SignUpUseCase
import javax.inject.Inject

class SignUpInteractor @Inject constructor(
    val signUp: SignUpUseCase,
)
