package ru.zarina.zarina.data.rework.location

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import ru.zarina.zarina.domain.exception.MissingPermissionException
import ru.zarina.zarina.domain.rework.Location
import timber.log.Timber
import javax.inject.Inject

class GooglePlayServicesLocationDataSource @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : LocationDataSource {
    override suspend fun getCurrentLocation(): Location? {
        try {
            val androidLocation = fusedLocationProviderClient
                .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .await()
            val location = androidLocation?.toLocation()
            Timber.v("Current location: $location")
            return location
        } catch (e: SecurityException) {
            throw MissingPermissionException("Missing location permission")
        }
    }

    private fun android.location.Location.toLocation(): Location = Location(latitude, longitude)
}
