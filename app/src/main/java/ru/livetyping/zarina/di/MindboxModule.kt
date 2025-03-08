package ru.livetyping.zarina.di

import cloud.mindbox.mobile_sdk.Mindbox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.MindboxDeviceUuidProvider
import ru.livetyping.zarina.core.buildutil.MindboxEndpoint
import ru.livetyping.zarina.core.buildutil.MindboxKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MindboxModule {

    @Provides
    @Singleton
    fun provideMindboxDeviceUuidProvider(): MindboxDeviceUuidProvider {
        return getMindboxDeviceUuidProvider()
    }

    @Provides
    @MindboxEndpoint
    fun provideMindboxEndpoint(): String = BuildConfig.MINDBOX_ENDPOINT

    @Provides
    @MindboxKey
    fun provideMindboxKey(): String = BuildConfig.MINDBOX_KEY

    private fun getMindboxDeviceUuidProvider(): MindboxDeviceUuidProvider {
        return object : MindboxDeviceUuidProvider {
            private val deviceUuid = MutableStateFlow<String?>(null)

            init {
                Mindbox.subscribeDeviceUuid { deviceUuid.value = it }
            }

            override fun getMindboxDeviceUuidFlow(): Flow<String?> = deviceUuid.asStateFlow()
        }
    }
}
