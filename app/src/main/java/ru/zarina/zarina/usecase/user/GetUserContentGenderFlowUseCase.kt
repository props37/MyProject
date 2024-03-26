package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.user.UserRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.common.Gender
import javax.inject.Inject

class GetUserContentGenderFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : FlowUseCase<Unit, Gender>(dispatcher) {

    override fun execute(params: Unit): Flow<Gender> {
        return userRepository.getUserContentGenderFlow()
            .map { it ?: Gender.getDefault() }
    }
}
