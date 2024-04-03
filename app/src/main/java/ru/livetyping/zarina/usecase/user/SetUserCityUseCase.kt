package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.usecase.cart.FetchCartProductIdsUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import timber.log.Timber
import javax.inject.Inject

class SetUserCityUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val fetchCartProductIdsUseCase: FetchCartProductIdsUseCase,
) : UseCase<SetUserCityUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val city = params.city
        Timber.v("Set user city: $city")
        userRepository.setUserCity(city)
        fetchCartProductIdsUseCase()
    }

    data class Params(val city: City)
}
