package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetInAppReviewRequestFlowUseCase {
    public operator fun invoke(): Flow<Unit>

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetInAppReviewRequestFlowUseCase {
            return GetInAppReviewRequestFlowUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}