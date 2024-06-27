package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class FetchLoyaltyCardUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Fetch loyalty card")
        val user = userRepository.getUserFlow().firstOrNull()
        if (user != null) {
            userRepository.fetchLoyaltyCard()
        } else {
            Timber.v("Do not fetch loyalty card because user is not logged in")
        }
    }
}
