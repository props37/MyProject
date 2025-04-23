package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetInAppReviewRequestFlowUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : GetInAppReviewRequestFlowUseCase {

    override fun invoke(): Flow<Unit> {
        return userRepository.getInAppReviewRequestFlow()
    }

    private companion object {
        private const val TAG = "GetInAppReviewRequestFlowUseCaseImpl"
    }
}
