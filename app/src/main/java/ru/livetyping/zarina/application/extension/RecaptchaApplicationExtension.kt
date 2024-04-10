package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.data.recaptcha.RecaptchaManager
import javax.inject.Inject

class RecaptchaApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val recaptchaManager: RecaptchaManager,
) : ApplicationExtension {

    override fun install(application: Application) {
        coroutineScope.launch {
            recaptchaManager.init(application)
        }
    }
}
