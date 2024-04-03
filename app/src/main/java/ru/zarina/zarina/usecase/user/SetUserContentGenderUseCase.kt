package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.user.UserRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.common.Gender
import timber.log.Timber
import javax.inject.Inject

class SetUserContentGenderUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<SetUserContentGenderUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val gender = params.gender
        Timber.v("Set user content gender: $gender")
        userRepository.setUserContentGender(gender)
    }

    data class Params(val gender: Gender)
}
