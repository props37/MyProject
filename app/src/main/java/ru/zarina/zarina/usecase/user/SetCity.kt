package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.user.IUserRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber

@Factory
class SetCityUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository,
) : UseCase<City, Unit>(dispatcher) {
    override suspend fun execute(params: City) {
        userRepository.setCity(city = params)
        Timber.v("Set current user city to $params")
    }
}
