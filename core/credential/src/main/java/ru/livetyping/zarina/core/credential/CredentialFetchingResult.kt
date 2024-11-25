package ru.livetyping.zarina.core.credential

public sealed interface CredentialFetchingResult {
    public data class Success(val username: String, val password: String) : CredentialFetchingResult

    public data object Cancelled : CredentialFetchingResult

    public data class Failure(val throwable: Throwable) : CredentialFetchingResult
}
