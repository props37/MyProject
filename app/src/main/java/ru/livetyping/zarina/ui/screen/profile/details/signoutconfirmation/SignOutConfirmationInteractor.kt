package ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation

import ru.livetyping.zarina.usecase.user.SignOutUseCase
import javax.inject.Inject

class SignOutConfirmationInteractor @Inject constructor(
    val signOut: SignOutUseCase,
)
