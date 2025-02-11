package ru.livetyping.zarina.core.domain.usecase.onboarding

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetIsOnboardingCompletedFlowUseCase {
    public operator fun invoke(): Flow<Result<Boolean>>

    public companion object {
        public fun getInstance(
            onboardingRepository: OnboardingRepository,
            logger: UseCaseLogger?,
        ): GetIsOnboardingCompletedFlowUseCase {
            return GetIsOnboardingCompletedFlowUseCaseImpl(
                onboardingRepository = onboardingRepository,
                logger = logger,
            )
        }
    }
}
