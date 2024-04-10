package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Gender
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
