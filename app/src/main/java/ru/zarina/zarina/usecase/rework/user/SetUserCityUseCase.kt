package ru.zarina.zarina.usecase.rework.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.user.UserRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.usecase.base.UseCase
import ru.zarina.zarina.usecase.rework.cart.FetchCartProductIdsUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class SetUserCityUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val fetchCartProductIdsUseCase: FetchCartProductIdsUseCase,
) : UseCase<SetUserCityUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val city = params.city
        userRepository.setUserCity(city)
        fetchCartProductIdsUseCase()
    }

    data class Params(val city: City)
}
