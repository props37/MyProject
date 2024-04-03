package ru.livetyping.zarina.data.old.location.source

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.common.exception.MissingPermissionException
import ru.livetyping.zarina.domain.old.GeoLocation

@Factory
class PlayServicesGeoLocationSource(
    private val client: FusedLocationProviderClient,
) : IGeoLocationSource {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentLocation(): GeoLocation? {
        val cancellationTokenSource = CancellationTokenSource()

        try {
            val location = client
                .getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    cancellationTokenSource.token
                )
                .await(cancellationTokenSource)
            return location?.toDomain()
        } catch (exception: SecurityException) {
            throw MissingPermissionException("Missing location permission")
        }
    }

    private fun Location.toDomain(): GeoLocation = GeoLocation(latitude, longitude)

}
