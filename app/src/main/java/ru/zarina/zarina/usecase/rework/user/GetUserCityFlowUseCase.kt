package ru.zarina.zarina.usecase.rework.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.user.UserRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.base.usecase.FlowUseCase
import javax.inject.Inject

class GetUserCityFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : FlowUseCase<Unit, City?>(dispatcher) {

    override fun execute(params: Unit): Flow<City?> {
        return userRepository.getUserCityFlow()
    }
}
