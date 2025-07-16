package ru.livetyping.zarina.data.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.data.auth.AuthRepositoryImpl
import ru.livetyping.zarina.data.auth.local.AuthLocalDataSource
import ru.livetyping.zarina.data.auth.local.AuthLocalDataSourceImpl
import ru.livetyping.zarina.data.auth.local.storage.AuthEncryptedStorage
import ru.livetyping.zarina.data.auth.local.storage.AuthEncryptedStorageImpl
import ru.livetyping.zarina.data.auth.remote.AuthRemoteDataSource
import ru.livetyping.zarina.data.auth.remote.AuthRemoteDataSourceImpl
import ru.livetyping.zarina.data.auth.remote.api.AuthApi
import ru.livetyping.zarina.data.auth.remote.api.AuthApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindAuthLocalDataSource(
        impl: AuthLocalDataSourceImpl,
    ): AuthLocalDataSource

    @Binds
    abstract fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    abstract fun bindAuthEncryptedStorage(
        impl: AuthEncryptedStorageImpl,
    ): AuthEncryptedStorage

    @Binds
    abstract fun bindAuthApi(impl: AuthApiImpl): AuthApi
}
