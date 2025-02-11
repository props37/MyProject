package ru.livetyping.zarina.core.domain.usecase.onboarding

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetIsOnboardingCompletedFlowUseCaseImpl(
    private val onboardingRepository: OnboardingRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Boolean>(logger), GetIsOnboardingCompletedFlowUseCase {

    override fun execute(params: Unit): Flow<Boolean> {
        return onboardingRepository.getIsOnboardingCompleted()
    }

    override fun invoke(): Flow<Result<Boolean>> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "GetIsOnboardingCompletedFlowUseCaseImpl"
    }
}
