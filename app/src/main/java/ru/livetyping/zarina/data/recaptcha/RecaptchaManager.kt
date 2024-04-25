package ru.livetyping.zarina.data.recaptcha

import android.app.Application
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.android.recaptcha.RecaptchaException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.domain.common.Token
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class RecaptchaManager @Inject constructor() {

    @Volatile
    private var client: RecaptchaClient? = null

    private var applicationRef: WeakReference<Application>? = null
    private val initMutex = Mutex()

    suspend fun init(application: Application) {
        applicationRef = WeakReference(application)
        getClient(application)
    }

    suspend fun execute(action: RecaptchaAction): Token = withTimeout(TIMEOUT) {
        val application = applicationRef?.get()
        checkNotNull(application) { "Application is null. Did you forget to call RecaptchaManager.init()?" }

        val client = getClient(application)
        checkNotNull(client) { "RecaptchaClient is not initialized" }

        val token = client.execute(action, TIMEOUT.inWholeMilliseconds).getOrThrow()
        Token(token)
    }

    private suspend fun getClient(application: Application): RecaptchaClient? = withTimeout(TIMEOUT) {
        val currentClient = client
        if (currentClient != null) {
            Timber.tag(TAG).v(MESSAGE_ALREADY_INITIALIZED)
            currentClient
        } else {
            initMutex.withLock {
                client?.let {
                    Timber.tag(TAG).v(MESSAGE_ALREADY_INITIALIZED)
                    return@withLock it
                }

                Timber.tag(TAG).v("Initialize Recaptcha client")
                val result = Recaptcha.getClient(
                    application = application,
                    siteKey = BuildConfig.RECAPTCHA_KEY,
                    timeout = TIMEOUT.inWholeMilliseconds,
                )
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
    }

    companion object {
        val ACTION_SIGN_UP: RecaptchaAction
            get() = RecaptchaAction.custom("register_android")

        val ACTION_SIGN_IN_BY_EMAIL: RecaptchaAction
            get() = RecaptchaAction.custom("auth_email_android")

        val ACTION_SIGN_IN_BY_PHONE: RecaptchaAction
            get() = RecaptchaAction.custom("auth_phone_android")

        private const val TAG = "RecaptchaManager"

        private val TIMEOUT = 10.seconds

        private const val MESSAGE_ALREADY_INITIALIZED =
            "Recaptcha client is already initialized, return it"
    }
}
