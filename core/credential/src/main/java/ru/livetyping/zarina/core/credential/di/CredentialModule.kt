package ru.livetyping.zarina.core.credential.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.credential.CredentialManager
import ru.livetyping.zarina.core.credential.impl.CredentialManagerImpl
import javax.inject.Singleton
import androidx.credentials.CredentialManager as JetpackCredentialManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CredentialModule {
    @Binds
    abstract fun bindCredentialManager(
        impl: CredentialManagerImpl,
    ): CredentialManager

    companion object {
        @Provides
        @Singleton
        fun provideJetpackCredentialManager(
            @ApplicationContext
            context: Context,
        ): JetpackCredentialManager {
            return JetpackCredentialManager.create(context)
        }
    }
}
