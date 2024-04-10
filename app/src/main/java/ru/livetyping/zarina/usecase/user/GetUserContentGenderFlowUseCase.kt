package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Gender
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
