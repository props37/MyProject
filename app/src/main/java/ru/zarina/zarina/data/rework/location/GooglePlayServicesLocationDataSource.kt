package ru.zarina.zarina.data.rework.location

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import ru.zarina.zarina.domain.exception.MissingPermissionException
import ru.zarina.zarina.domain.rework.location.Location
import javax.inject.Inject

class GooglePlayServicesLocationDataSource @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : LocationDataSource {
    override suspend fun getCurrentLocation(): Location? {
        return withTimeout(LocationDataSource.CURRENT_LOCATION_DETECTING_TIMEOUT) {
            try {
                val androidLocation = fusedLocationProviderClient
                    .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .await()
                androidLocation?.toLocation()
            } catch (e: SecurityException) {
                throw MissingPermissionException("Missing location permission")
            }
        }
    }

    private fun android.location.Location.toLocation(): Location {
        return Location(latitude, longitude)
    }
}
