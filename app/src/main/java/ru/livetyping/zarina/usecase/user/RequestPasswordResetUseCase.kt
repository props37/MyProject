package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Email
import timber.log.Timber
import javax.inject.Inject

class RequestPasswordResetUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
) : UseCase<RequestPasswordResetUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val email = params.email
        Timber.v("Request password reset for email $email")

        validateEmailUseCase(ValidateEmailUseCase.Params(email)).getOrThrow()

        userRepository.requestPasswordReset(email)
    }

    data class Params(val email: Email)
}
