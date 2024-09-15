package ru.livetyping.zarina.presentation.common.credentialmanager

interface CredentialFetchingResult {
    data class Success(val username: String, val password: String) : CredentialFetchingResult

    data object Cancelled : CredentialFetchingResult

    data class Failure(val throwable: Throwable) : CredentialFetchingResult
}
