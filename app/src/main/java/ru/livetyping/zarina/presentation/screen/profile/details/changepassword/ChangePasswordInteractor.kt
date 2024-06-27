package ru.livetyping.zarina.presentation.screen.profile.details.changepassword

import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.usecase.user.UpdateUserInfoUseCase
import javax.inject.Inject

class ChangePasswordInteractor @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val updateUserInfo: UpdateUserInfoUseCase,
)
