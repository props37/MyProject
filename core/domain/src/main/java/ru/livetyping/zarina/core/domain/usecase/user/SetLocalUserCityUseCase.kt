package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SetLocalUserCityUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val city: City)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): SetLocalUserCityUseCase {
            return SetLocalUserCityUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
