package ru.livetyping.zarina.core.domain.usecase.onboarding

import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.core.domain.usecase.onboarding.SetIsOnboardingCompletedUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SetIsOnboardingCompletedUseCaseImpl(
    private val onboardingRepository: OnboardingRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SetIsOnboardingCompletedUseCase {

    override suspend fun execute(params: Params) {
        onboardingRepository.setIsOnboardingCompleted(params.isCompleted)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }
}
