package ru.livetyping.zarina.feature.onboarding.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.feature.onboarding.domain.repository.OnboardingRepository

internal class GetOnboardingBannerUrlFlowUseCaseImpl(
    private val onboardingRepository: OnboardingRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Url>(logger), GetOnboardingBannerUrlFlowUseCase {

    override fun execute(params: Unit): Flow<Url> {
        return onboardingRepository.getOnboardingBannerUrlFlow()
    }

    override fun invoke(): Flow<Result<Url>> {
        return call(Unit)
    }
}
