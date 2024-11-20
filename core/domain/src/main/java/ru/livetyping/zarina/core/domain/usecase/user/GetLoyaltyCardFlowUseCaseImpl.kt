package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetLoyaltyCardFlowUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, LoyaltyCard?>(logger), GetLoyaltyCardFlowUseCase {

    override fun execute(params: Params): Flow<LoyaltyCard?> {
        return userRepository.getLoyaltyCardFlow(params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<LoyaltyCard?>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetLoyaltyCardFlowUseCaseImpl"
    }
}
