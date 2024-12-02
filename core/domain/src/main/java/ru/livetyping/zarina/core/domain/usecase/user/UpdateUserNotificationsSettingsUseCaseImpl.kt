package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.UpdateUserNotificationsSettingsUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class UpdateUserNotificationsSettingsUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), UpdateUserNotificationsSettingsUseCase {

    override suspend fun execute(params: Params) {
        userRepository.updateUserNotificationSettings(
            receiveSms = params.receiveSms,
            receiveEmails = params.receiveEmails,
        )
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "UpdateUserNotificationsSettingsUseCaseImpl"
    }
}
