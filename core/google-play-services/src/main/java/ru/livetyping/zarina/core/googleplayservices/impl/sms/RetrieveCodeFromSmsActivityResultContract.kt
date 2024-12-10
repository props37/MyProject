package ru.livetyping.zarina.core.googleplayservices.impl.sms

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.phone.SmsRetriever

internal class RetrieveCodeFromSmsActivityResultContract(
    codeRegexPattern: String,
) : ActivityResultContract<Intent, String?>() {
    private val codeRegex = codeRegexPattern.toRegex()

    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode != Activity.RESULT_OK || intent == null) return null
        val message = intent.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
        return message?.let {
            codeRegex.find(message)?.value
        }
    }
}
