package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.MindboxRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ConfirmSignInByEmailUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val phone: PhoneNumber,
        val otp: String,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            authRepository: AuthRepository,
            mindboxRepository: MindboxRepository,
            appMetrica: AppMetrica,
            logger: UseCaseLogger?,
        ): ConfirmSignInByEmailUseCase {
            return ConfirmSignInByEmailUseCaseImpl(
                userRepository = userRepository,
                authRepository = authRepository,
                mindboxRepository = mindboxRepository,
                appMetrica = appMetrica,
                logger = logger,
            )
        }
    }
}
