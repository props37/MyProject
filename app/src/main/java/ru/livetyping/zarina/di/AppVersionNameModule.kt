package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.AppVersionName

@Module
@InstallIn(SingletonComponent::class)
class AppVersionNameModule {

    @Provides
    @AppVersionName
    fun provideAppVersionName(): String = BuildConfig.VERSION_NAME
}
