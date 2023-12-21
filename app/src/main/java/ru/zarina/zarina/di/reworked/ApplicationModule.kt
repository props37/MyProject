package ru.zarina.zarina.di.reworked

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zarina.zarina.data.permissionmanager.PermissionManager
import ru.zarina.zarina.data.permissionmanager.PermissionManagerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindPermissionManager(
        permissionManagerImpl: PermissionManagerImpl,
    ): PermissionManager
}
