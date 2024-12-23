package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetLoyaltyProgramExpectedBonusesPageFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<Page<List<LoyaltyProgramBonusAction>>>>

    public data class Params(val page: Int)

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetLoyaltyProgramExpectedBonusesPageFlowUseCase {
            return GetLoyaltyProgramExpectedBonusesPageFlowUseCaseImpl(
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}
