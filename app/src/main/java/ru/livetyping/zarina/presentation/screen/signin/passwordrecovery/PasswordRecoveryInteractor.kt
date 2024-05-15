package ru.livetyping.zarina.presentation.screen.signin.passwordrecovery

import ru.livetyping.zarina.usecase.user.RequestPasswordResetUseCase
import javax.inject.Inject

class PasswordRecoveryInteractor @Inject constructor(
    val requestPasswordReset: RequestPasswordResetUseCase,
)
