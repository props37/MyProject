package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

import ru.livetyping.zarina.core.domain.usecase.user.DeleteAccountUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignOutUseCase
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserInfoUseCase
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserNotificationsSettingsUseCase
import javax.inject.Inject

internal class ProfileDetailsDependencies @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val updateUserInfo: UpdateUserInfoUseCase,
    val updateUserNotificationsSettings: UpdateUserNotificationsSettingsUseCase,
    val signOut: SignOutUseCase,
    val deleteAccount: DeleteAccountUseCase,
)
