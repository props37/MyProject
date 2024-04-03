package ru.livetyping.zarina.usecase.old.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.user.IUserRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.City
import timber.log.Timber

@Factory
class GetCityUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val userRepository: IUserRepository,
) : UseCase<Unit, City>(dispatcher) {
    override suspend fun execute(params: Unit): City {
        val city = userRepository.getCity().first()
        Timber.v(
            buildString {
                append("Current user city: $city")
                if (city == null) append(", using default city ${City.DEFAULT}")
            }
        )
        return city ?: City.DEFAULT
    }
}
