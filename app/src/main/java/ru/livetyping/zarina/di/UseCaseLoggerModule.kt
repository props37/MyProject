package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
internal class UseCaseLoggerModule {

    @Provides
    fun provideUseCaseLogger(): UseCaseLogger {
        return getUseCaseLogger()
    }

    private fun getUseCaseLogger(): UseCaseLogger {
        return object : UseCaseLogger {
            override fun v(tag: String, message: String) {
                Timber.tag(tag).v(message)
            }

            override fun v(tag: String, throwable: Throwable, message: String) {
                Timber.tag(tag).v(throwable, message)
            }

            override fun e(tag: String, message: String) {
                Timber.tag(tag).e(message)
            }

            override fun e(tag: String, throwable: Throwable, message: String) {
                Timber.tag(tag).e(throwable, message)
            }
        }
    }
}
