package ru.livetyping.zarina.core.domain.usecase.onboarding

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetOnboardingBannerUrlFlowUseCase {
    public operator fun invoke(): Flow<Result<Url>>

    public companion object {
        public fun getInstance(
            onboardingRepository: OnboardingRepository,
            logger: UseCaseLogger?,
        ): GetOnboardingBannerUrlFlowUseCase {
            return GetOnboardingBannerUrlFlowUseCaseImpl(
                onboardingRepository = onboardingRepository,
                logger = logger,
            )
        }
    }
}
