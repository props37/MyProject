package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.domain.user.User
import ru.livetyping.zarina.usecase.authorization.ClearAuthorizationTokensUseCase
import ru.livetyping.zarina.usecase.authorization.SetAuthorizationTokensUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class SetUserWithAuthorizationTokensUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val setAuthorizationTokensUseCase: SetAuthorizationTokensUseCase,
    private val clearAuthorizationTokensUseCase: ClearAuthorizationTokensUseCase,
) : UseCase<SetUserWithAuthorizationTokensUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val tokens = params.authorizationTokens
        val user = params.user
        Timber.v("Set user $user with authorization tokens $tokens")

        val setTokensParams = SetAuthorizationTokensUseCase.Params(tokens)
        setAuthorizationTokensUseCase(setTokensParams).getOrThrow()

        try {
            userRepository.setUser(user)
        } catch (e: Exception) {
            clearAuthorizationTokensUseCase()
            throw e
        }
    }

    data class Params(
        val user: User,
        val authorizationTokens: AuthorizationTokens,
    )
}
