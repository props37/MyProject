package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.user.UserRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.common.Gender
import javax.inject.Inject

class SetUserContentGenderUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<SetUserContentGenderUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        userRepository.setUserContentGender(params.gender)
    }

    data class Params(val gender: Gender)
}
