package ru.livetyping.zarina.usecase.old.user

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.user.IUserRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.City
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
