package ru.zarina.zarina.data.recaptcha

import android.app.Application
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.android.recaptcha.RecaptchaException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.domain.common.Token
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecaptchaManager @Inject constructor() {
    private var applicationRef: WeakReference<Application>? = null
    private var client: RecaptchaClient? = null
    private val initMutex = Mutex()

    suspend fun init(application: Application) {
        applicationRef = WeakReference(application)
        getClient(application)
    }

    suspend fun execute(action: RecaptchaAction): Token {
        val application = applicationRef?.get()
        checkNotNull(application) { "Application is null. Did you forget to call RecaptchaManager.init()?" }

        val client = getClient(application)
        checkNotNull(client) { "RecaptchaClient is not initialized" }

        val token = client.execute(action).getOrThrow()
        return Token(token)
    }

    private suspend fun getClient(application: Application): RecaptchaClient? {
        return initMutex.withLock {
            client?.let {
                Timber.tag(TAG).v("Recaptcha client is already initialized, return it")
                return@withLock it
            }

            Timber.tag(TAG).v("Initialize Recaptcha client")
            val result = Recaptcha.getClient(application, BuildConfig.RECAPTCHA_KEY)
                .onSuccess { client = it }
                .onFailure { e ->
                    val message = if (e is RecaptchaException) {
                        "Recaptcha client initialization failed. Code: ${e.errorCode}, message: ${e.errorMessage}"
                    } else {
                        "Recaptcha client initialization failed"
                    }
                    Timber.tag(TAG).e(e, message)
                }
            result.getOrNull()
        }
    }

    companion object {
        private const val TAG = "RecaptchaManager"
    }
}
