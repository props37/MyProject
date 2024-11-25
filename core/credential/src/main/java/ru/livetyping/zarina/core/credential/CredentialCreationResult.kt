package ru.livetyping.zarina.core.credential

public sealed interface CredentialCreationResult {
    public data object Success : CredentialCreationResult

    public data object Cancelled : CredentialCreationResult

    public data class Failure(val throwable: Throwable) : CredentialCreationResult
}
