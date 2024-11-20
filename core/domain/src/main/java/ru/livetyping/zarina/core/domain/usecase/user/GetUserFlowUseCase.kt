package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetUserFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<User?>>

    public data class Params(val cachePolicy: CachePolicy)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetUserFlowUseCase {
            return GetUserFlowUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
