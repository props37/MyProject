package ru.zarina.zarina.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
class CoroutineModule {

    @Singleton
    fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Named(Qualifiers.Dispatcher.IO)
    fun providesDispatcherIo() = Dispatchers.IO

}


