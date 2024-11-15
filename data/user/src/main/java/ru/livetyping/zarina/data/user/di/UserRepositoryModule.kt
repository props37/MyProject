package ru.livetyping.zarina.data.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.data.user.impl.UserRepositoryImpl
import ru.livetyping.zarina.data.user.impl.local.UserLocalDataSource
import ru.livetyping.zarina.data.user.impl.local.UserLocalDataSourceImpl
import ru.livetyping.zarina.data.user.impl.local.city.UserCityDataHolder
import ru.livetyping.zarina.data.user.impl.local.city.UserCityDataHolderImpl
import ru.livetyping.zarina.data.user.impl.local.city.entity.CityEntity
import ru.livetyping.zarina.data.user.impl.remote.UserRemoteDataSource
import ru.livetyping.zarina.data.user.impl.remote.UserRemoteDataSourceImpl
import ru.livetyping.zarina.data.user.impl.remote.api.UserApi
import ru.livetyping.zarina.data.user.impl.remote.api.UserApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UserRepositoryModule {

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserRemoteDataSource(
        impl: UserRemoteDataSourceImpl,
    ): UserRemoteDataSource

    @Binds
    abstract fun bindUserApi(impl: UserApiImpl): UserApi

    @Binds
    abstract fun bindUserLocalDataSource(
        impl: UserLocalDataSourceImpl,
    ): UserLocalDataSource

    @Binds
    abstract fun bindUserCityDataHolder(
        impl: UserCityDataHolderImpl,
    ): UserCityDataHolder

    companion object {
        @Provides
        @Singleton
        fun provideCityEntityDataStore(
            @ApplicationContext
            context: Context,
            serializer: CityEntity.DataStoreSerializer,
        ): DataStore<CityEntity?> {
            return DataStoreFactory.create(
                serializer = serializer,
                produceFile = { context.dataStoreFile(CITY_ENTITY_DATA_STORE_NAME) },
            )
        }

        @Provides
        fun provideCityEntityDataStoreSerializer(): CityEntity.DataStoreSerializer {
            val json = Json {
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
                explicitNulls = false
            }
            return CityEntity.DataStoreSerializer(
                json = json,
                dispatcher = Dispatchers.IO,
            )
        }

        private const val CITY_ENTITY_DATA_STORE_NAME = "city_entity_data_store"
    }
}
