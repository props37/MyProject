package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.analytics.impl.AppMetricaImpl

@Module
@InstallIn(SingletonComponent::class)
class AnalyticsModule {
    @Provides
    fun provideAppMetrica(): AppMetrica {
        return AppMetricaImpl()
    }
}
