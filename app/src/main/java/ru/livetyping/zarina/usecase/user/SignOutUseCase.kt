package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.usecase.authorization.SetAuthorizationTokensUseCase
import timber.log.Timber
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val signOutCleanupUseCase: SignOutCleanupUseCase,
    private val setAuthorizationTokensUseCase: SetAuthorizationTokensUseCase,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Sign out")
        val tokens = userRepository.signOut()
        val signOutCleanupParams = SignOutCleanupUseCase.Params(clearAuthorizationTokens = false)
        signOutCleanupUseCase(signOutCleanupParams).getOrThrow()
        setAuthorizationTokensUseCase(SetAuthorizationTokensUseCase.Params(tokens))
    }
}
