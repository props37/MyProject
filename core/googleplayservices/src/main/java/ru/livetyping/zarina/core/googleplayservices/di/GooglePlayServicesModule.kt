package ru.livetyping.zarina.core.googleplayservices.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.googleplayservices.impl.sms.SmsCodeRetrieverImpl
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GooglePlayServicesModule {

    @Binds
    abstract fun bindSmsCodeRetriever(
        impl: SmsCodeRetrieverImpl,
    ): SmsCodeRetriever

    companion object {

        @Provides
        fun provideFusedLocationProviderClient(
            @ApplicationContext
            context: Context,
        ): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }
    }
}
