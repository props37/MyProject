package ru.livetyping.zarina.core.permission.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.permission.PermissionManager
import ru.livetyping.zarina.core.permission.impl.PermissionManagerImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PermissionManagerModule {

    @Binds
    abstract fun bindPermissionManager(impl: PermissionManagerImpl): PermissionManager
}
