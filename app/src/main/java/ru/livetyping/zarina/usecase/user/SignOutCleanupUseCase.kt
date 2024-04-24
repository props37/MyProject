package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.usecase.authorization.ClearAuthorizationTokensUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class SignOutCleanupUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val clearAuthorizationTokensUseCase: ClearAuthorizationTokensUseCase,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Sign out cleanup")
        clearAuthorizationTokensUseCase().getOrThrow()
        userRepository.clear()
        cartRepository.clear()
        favoriteRepository.clear()
    }
}
