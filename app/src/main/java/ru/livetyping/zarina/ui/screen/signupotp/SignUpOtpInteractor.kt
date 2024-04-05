package ru.livetyping.zarina.ui.screen.signupotp

import ru.livetyping.zarina.usecase.user.ConfirmSignUpUseCase
import javax.inject.Inject

class SignUpOtpInteractor @Inject constructor(
    val confirmSignUp: ConfirmSignUpUseCase,
)
