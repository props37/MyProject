package ru.livetyping.zarina.core.domain.usecase.onboarding

import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SetIsOnboardingCompletedUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val isCompleted: Boolean)

    public companion object {
        public fun getInstance(
            onboardingRepository: OnboardingRepository,
            logger: UseCaseLogger?,
        ): SetIsOnboardingCompletedUseCase {
            return SetIsOnboardingCompletedUseCaseImpl(
                onboardingRepository = onboardingRepository,
                logger = logger,
            )
        }
    }
}
