package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface UpdateUserNotificationsSettingsUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val receiveSms: Boolean,
        val receiveEmails: Boolean,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): UpdateUserNotificationsSettingsUseCase {
            return UpdateUserNotificationsSettingsUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
