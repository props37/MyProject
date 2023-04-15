package ru.zarina.zarina.usecase.location

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.location.IGeoLocationRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import timber.log.Timber
import javax.inject.Inject

class DetectCityUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val geoLocationRepository: IGeoLocationRepository,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(params: Unit) {
        val geoLocation = geoLocationRepository.getCurrentLocation()
        // TODO if geoLocation is null, show "location services are not available"
        Timber.v("Current location: $geoLocation")
    }
}
