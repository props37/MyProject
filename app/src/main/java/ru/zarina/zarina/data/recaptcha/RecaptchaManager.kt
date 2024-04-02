package ru.zarina.zarina.data.recaptcha

import android.app.Application
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaClient
import com.google.android.recaptcha.RecaptchaException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.zarina.zarina.BuildConfig
import timber.log.Timber
import javax.inject.Inject

class RecaptchaManager @Inject constructor() {
    private val initMutex = Mutex()
    private var client: RecaptchaClient? = null

    suspend fun init(application: Application) {
        getClient(application)
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
                    if (e is RecaptchaException) {
                        Timber.tag(TAG).e(
                            e,
                            "Recaptcha client initialization failed. " +
                                    "Code: ${e.errorCode}, message: ${e.errorMessage}"
                        )
                    } else {
                        Timber.tag(TAG).e(e, "Recaptcha client initialization failed")
                    }
                }

            result.getOrNull()
        }
    }

    companion object {
        private const val TAG = "RecaptchaManager"
    }
}
