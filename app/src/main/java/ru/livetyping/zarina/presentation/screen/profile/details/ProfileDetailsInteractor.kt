package ru.livetyping.zarina.presentation.screen.profile.details

import ru.livetyping.zarina.usecase.user.GetRemoteUserFlowUseCase
import ru.livetyping.zarina.usecase.user.UpdateUserInfoUseCase
import javax.inject.Inject

class ProfileDetailsInteractor @Inject constructor(
    val getRemoteUserFlow: GetRemoteUserFlowUseCase,
    val updateUserInfo: UpdateUserInfoUseCase,
)
