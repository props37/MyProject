package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.user.UserRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.usecase.cart.FetchCartProductIdsUseCase
import ru.zarina.zarina.util.base.usecase.invoke
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
