package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.AppVersionCode

@Module
@InstallIn(SingletonComponent::class)
class AppVersionCodeModule {

    @Provides
    @AppVersionCode
    fun provideAppVersionName(): Int = BuildConfig.VERSION_CODE
}
