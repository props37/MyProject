package ru.livetyping.zarina.core.credential

import android.content.Context
import ru.livetyping.zarina.core.credential.impl.CredentialManagerImpl
import androidx.credentials.CredentialManager as JetpackCredentialManager

public interface CredentialManager {
    public suspend fun getCredential(): CredentialFetchingResult

    public suspend fun createCredential(
        username: String,
        password: String,
    ): CredentialCreationResult

    public companion object {
        public fun createInstance(
            context: Context,
            jetpackCredentialManager: JetpackCredentialManager,
        ): CredentialManager = CredentialManagerImpl(context, jetpackCredentialManager)
    }
}
