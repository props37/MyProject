package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.user.LoyaltyCard
import javax.inject.Inject

class GetLoyaltyCardFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : FlowUseCase<Unit, LoyaltyCard>(dispatcher) {

    override fun execute(params: Unit): Flow<LoyaltyCard> {
        return userRepository.getLoyaltyCardFlow()
    }
}
