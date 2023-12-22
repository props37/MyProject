package ru.zarina.zarina.di.reworked

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

    @Provides
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.MAIN_IMMEDIATE)
    fun provideMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    @Provides
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.DEFAULT)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
