package ru.livetyping.zarina.presentation.screen.profile.details

import ru.livetyping.zarina.usecase.user.GetUpdatedUserFlowUseCase
import ru.livetyping.zarina.usecase.user.UpdateUserInfoUseCase
import ru.livetyping.zarina.usecase.user.UpdateUserNotificationSettingsUseCase
import javax.inject.Inject

class ProfileDetailsInteractor @Inject constructor(
    val getUpdatedUserFlow: GetUpdatedUserFlowUseCase,
    val updateUserInfo: UpdateUserInfoUseCase,
    val updateUserNotificationSettings: UpdateUserNotificationSettingsUseCase,
)
