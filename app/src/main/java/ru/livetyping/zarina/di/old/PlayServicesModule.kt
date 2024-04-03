package ru.livetyping.zarina.di.old

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class PlayServicesModule {

    @Factory
    fun providesFusedLocationProviderClient(
        context: Context,
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

}
