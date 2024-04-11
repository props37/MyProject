package ru.livetyping.zarina.ui.screen.signin

import ru.livetyping.zarina.usecase.user.SignInByEmailUseCase
import javax.inject.Inject

class SignInInteractor @Inject constructor(
    val signInByEmail: SignInByEmailUseCase,
)
