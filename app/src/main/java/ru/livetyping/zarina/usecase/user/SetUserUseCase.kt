package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.user.User
import timber.log.Timber
import javax.inject.Inject

class SetUserUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<SetUserUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val user = params.user
        Timber.v("Set user: $user")
        userRepository.setUser(user)
    }

    data class Params(val user: User)
}
