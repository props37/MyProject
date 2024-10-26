package ru.livetyping.zarina.feature.onboarding.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.feature.onboarding.domain.repository.OnboardingRepository

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
