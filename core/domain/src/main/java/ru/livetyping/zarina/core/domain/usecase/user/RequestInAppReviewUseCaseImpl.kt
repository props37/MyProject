package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class RequestInAppReviewUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : RequestInAppReviewUseCase {

    override fun invoke() {
        userRepository.requestInAppReview()
    }

    private companion object {
        private const val TAG = "RequestInAppReviewUseCaseImpl"
    }
}
