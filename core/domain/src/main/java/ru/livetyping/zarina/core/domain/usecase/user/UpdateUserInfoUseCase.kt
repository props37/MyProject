package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import java.time.LocalDate

public interface UpdateUserInfoUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val firstName: String? = null,
        val lastName: String? = null,
        val birthDate: LocalDate? = null,
        val email: Email? = null,
        val gender: Gender? = null,
        val oldPassword: String? = null,
        val newPassword: String? = null,
    )

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): UpdateUserInfoUseCase {
            return UpdateUserInfoUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
