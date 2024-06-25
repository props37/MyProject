package ru.livetyping.zarina.presentation.screen.profile.details.changeemail

import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.usecase.user.UpdateUserInfoUseCase
import javax.inject.Inject

class ChangeEmailInteractor @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val updateUserInfo: UpdateUserInfoUseCase,
)
