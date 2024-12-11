package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SetUserCityUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val city: City)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): SetUserCityUseCase {
            return SetUserCityUseCaseImpl(
                userRepository = userRepository,
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
