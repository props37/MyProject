package ru.zarina.zarina.usecase.rework.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.user.UserRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.usecase.base.UseCase
import timber.log.Timber
import javax.inject.Inject

class SetDefaultUserCityUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        val defaultCity = City.DEFAULT
        Timber.v("Set default user city: $defaultCity")
        userRepository.setLocalUserCity(defaultCity)
    }
}
