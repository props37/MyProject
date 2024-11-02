package ru.livetyping.zarina.data.location.impl

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import ru.livetyping.zarina.core.domain.exception.MissingPermissionException
import ru.livetyping.zarina.core.domain.model.common.Location
import timber.log.Timber
import javax.inject.Inject

internal class GooglePlayServicesLocationDataSource @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : LocationDataSource {
    override suspend fun getCurrentLocation(): Location? {
        return withTimeout(CURRENT_LOCATION_DETECTION_TIMEOUT_MILLIS) {
            try {
                val androidLocation = fusedLocationProviderClient
                    .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                    .await()
                val location = androidLocation?.toLocation()
                Timber.tag(TAG).v("Current location: $location")
                location
            } catch (e: SecurityException) {
                throw MissingPermissionException("Missing location permission")
            }
        }
    }

    private fun android.location.Location.toLocation(): Location {
        return Location(latitude, longitude)
    }

    private companion object {
        const val CURRENT_LOCATION_DETECTION_TIMEOUT_MILLIS = 5_000L

        private const val TAG = "GooglePlayServicesLocationDataSource"
    }
}
