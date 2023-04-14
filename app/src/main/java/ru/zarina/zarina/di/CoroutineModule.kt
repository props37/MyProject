package ru.zarina.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

    @Singleton
    @Provides
    fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Dispatcher(ZarinaDispatcher.IO)
    @Provides
    fun providesDispatcherIo() = Dispatchers.IO

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(
    @Suppress("unused")
    val dispatcher: ZarinaDispatcher,
)

enum class ZarinaDispatcher { IO }
