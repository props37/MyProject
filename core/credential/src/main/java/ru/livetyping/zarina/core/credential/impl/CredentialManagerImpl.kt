package ru.livetyping.zarina.core.credential.impl

import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.livetyping.zarina.core.credential.CredentialCreationResult
import ru.livetyping.zarina.core.credential.CredentialFetchingResult
import ru.livetyping.zarina.core.credential.CredentialManager
import timber.log.Timber
import javax.inject.Inject
import androidx.credentials.CredentialManager as JetpackCredentialManager

internal class CredentialManagerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val jetpackCredentialManager: JetpackCredentialManager,
) : CredentialManager {

    override suspend fun getCredential(): CredentialFetchingResult {
        return try {
            val request = GetCredentialRequest(
                credentialOptions = listOf(GetPasswordOption()),
            )
            val response = jetpackCredentialManager.getCredential(
                context = context,
                request = request,
            )
            val credential = response.credential
            if (credential !is PasswordCredential) {
                val error = IllegalStateException("Wrong credential type $credential")
                return CredentialFetchingResult.Failure(error)
            }
            CredentialFetchingResult.Success(
                username = credential.id,
                password = credential.password,
            )
        } catch (e: GetCredentialCancellationException) {
            Timber.tag(TAG).e(e, "Credential retrieval cancelled")
            CredentialFetchingResult.Cancelled
        } catch (e: NoCredentialException) {
            Timber.tag(TAG).e(e, "No credential found")
            CredentialFetchingResult.Failure(e)
        } catch (e: GetCredentialException) {
            Timber.tag(TAG).e(e, "Failed to retrieve credential")
            CredentialFetchingResult.Failure(e)
        }
    }

    override suspend fun createCredential(
        username: String,
        password: String
    ): CredentialCreationResult {
        return try {
            val request = CreatePasswordRequest(id = username, password = password)
            jetpackCredentialManager.createCredential(
                context = context,
                request = request,
            )
            CredentialCreationResult.Success
        } catch (e: CreateCredentialCancellationException) {
            Timber.tag(TAG).e(e, "Credential creation cancelled")
            CredentialCreationResult.Cancelled
        } catch (e: CreateCredentialException) {
            Timber.tag(TAG).e(e, "Failed to create credential")
            CredentialCreationResult.Failure(e)
        }
    }

    private companion object {
        private const val TAG = "CredentialManagerImpl"
    }
}
