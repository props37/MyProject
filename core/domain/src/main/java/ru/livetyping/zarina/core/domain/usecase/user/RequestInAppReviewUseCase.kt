package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RequestInAppReviewUseCase {
    public operator fun invoke()

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): RequestInAppReviewUseCase {
            return RequestInAppReviewUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
