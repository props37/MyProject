package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.permission.PermissionManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PermissionManagerModule {

    @Provides
    @Singleton
    fun providePermissionManager(): PermissionManager = PermissionManager.createInstance()
}
