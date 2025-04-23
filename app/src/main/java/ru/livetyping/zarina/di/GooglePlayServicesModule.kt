package ru.livetyping.zarina.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.googleplayservices.review.InAppReviewManager
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GooglePlayServicesModule {
    @Provides
    @Singleton
    fun provideSmsCodeRetriever(
        @ApplicationContext
        context: Context,
    ): SmsCodeRetriever {
        return SmsCodeRetriever.createInstance(context)
    }

    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext
        context: Context,
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideInAppReviewManager(): InAppReviewManager {
        return InAppReviewManager.createInstance()
    }
}
