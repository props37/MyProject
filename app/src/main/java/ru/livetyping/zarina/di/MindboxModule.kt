package ru.livetyping.zarina.di

import cloud.mindbox.mobile_sdk.Mindbox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MindboxModule {

    companion object {
        @Provides
        @Singleton
        fun provideMindboxDeviceUuidProvider(): MindboxDeviceUuidProvider {
            return object : MindboxDeviceUuidProvider {
                private val deviceUuidFlow = MutableStateFlow<String?>(null)

                init {
                    Mindbox.subscribeDeviceUuid { deviceUuidFlow.value = it }
                }

                override fun getMindboxDeviceUuidFlow(): Flow<String?> {
                    return deviceUuidFlow.asStateFlow()
                }
            }
        }
    }
}

interface MindboxDeviceUuidProvider {
    fun getMindboxDeviceUuidFlow(): Flow<String?>
}
