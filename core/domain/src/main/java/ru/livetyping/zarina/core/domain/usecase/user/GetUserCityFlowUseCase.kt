package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetUserCityFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<City?>>

    public data class Params(val cachePolicy: CachePolicy)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetUserCityFlowUseCase {
            return GetUserCityFlowUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
