package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.authorization.AuthorizationRepository
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val authorizationRepository: AuthorizationRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Sign out")
        userRepository.signOut()

        authorizationRepository.clear()
        userRepository.clear()
        cartRepository.clear()
        favoriteRepository.clear()
    }
}
