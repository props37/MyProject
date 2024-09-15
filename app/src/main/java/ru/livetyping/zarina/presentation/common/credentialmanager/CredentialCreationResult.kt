package ru.livetyping.zarina.presentation.common.credentialmanager

sealed interface CredentialCreationResult {
    data object Success : CredentialCreationResult

    data object Cancelled : CredentialCreationResult

    data class Failure(val throwable: Throwable) : CredentialCreationResult
}
