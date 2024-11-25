package ru.livetyping.zarina.core.credential

public interface CredentialManager {
    public suspend fun getCredential(): CredentialFetchingResult

    public suspend fun createCredential(
        username: String,
        password: String,
    ): CredentialCreationResult
}
