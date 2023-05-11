package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.user.IUserRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import timber.log.Timber
import javax.inject.Inject

class SetCityUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository,
) : UseCase<City, Unit>(dispatcher) {
    override suspend fun execute(params: City) {
        userRepository.setCity(city = params)
        Timber.v("Set current user city to $params")
    }
}
