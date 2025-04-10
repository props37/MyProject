package ru.livetyping.zarina.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.credential.CredentialManager
import androidx.credentials.CredentialManager as JetpackCredentialManager

@Module
@InstallIn(SingletonComponent::class)
class CredentialModule {
    @Provides
    fun provideCredentialManager(
        @ApplicationContext
        context: Context,
    ): CredentialManager {
        val jetpackCredentialManager = JetpackCredentialManager.create(context)
        return CredentialManager.createInstance(context, jetpackCredentialManager)
    }
}
