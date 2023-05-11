package ru.zarina.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.user.UserRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import timber.log.Timber
import javax.inject.Inject

class GetCityUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
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
