package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyProgramBonusHistoryPageFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetLoyaltyProgramBonusHistoryPageFlowUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Page<List<LoyaltyProgramBonusAction>>>(logger),
    GetLoyaltyProgramBonusHistoryPageFlowUseCase {

    override fun execute(params: Params): Flow<Page<List<LoyaltyProgramBonusAction>>> {
        return userRepository.getLoyaltyCardBonusHistoryPageFlow(params.page)
    }

    override fun invoke(params: Params): Flow<Result<Page<List<LoyaltyProgramBonusAction>>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetLoyaltyProgramBonusHistoryPageFlowUseCaseImpl"
    }
}
