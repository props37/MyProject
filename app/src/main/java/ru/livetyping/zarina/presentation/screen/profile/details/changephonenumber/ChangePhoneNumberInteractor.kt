package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber

import ru.livetyping.zarina.usecase.user.ChangePhoneNumberUseCase
import javax.inject.Inject

class ChangePhoneNumberInteractor @Inject constructor(
    val changePhoneNumber: ChangePhoneNumberUseCase,
)
