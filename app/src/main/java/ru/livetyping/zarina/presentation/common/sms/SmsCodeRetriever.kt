package ru.livetyping.zarina.presentation.common.sms

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.livetyping.zarina.presentation.activity.ActivityResultRegistryHolder
import ru.livetyping.zarina.presentation.common.sms.activityresult.RetrieveCodeFromSmsActivityResultContract
import ru.livetyping.zarina.util.platform.BundleCompat
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsCodeRetriever @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val activityResultRegistryHolder: ActivityResultRegistryHolder,
) : AutoCloseable {

    private val listeners = mutableListOf<Listener>()

    private val smsRetrieverClient by lazy {
        SmsRetriever.getClient(context)
    }

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private val receiver by lazy {
        getBroadcastReceiver()
    }

    fun start(sender: String, codeRegexPattern: String) {
        stop()

        smsRetrieverClient.startSmsUserConsent(sender)
        activityResultLauncher = getActivityResultLauncher(codeRegexPattern)
        registerReceiver()
    }

    fun stop() {
        activityResultLauncher?.unregister()
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            Timber.tag(TAG).e(e, "Failed to unregister BroadcastReceiver")
        }
    }

    fun release() {
        listeners.clear()
        stop()
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    private fun getActivityResultLauncher(codeRegexPattern: String): ActivityResultLauncher<Intent> {
        val registry = activityResultRegistryHolder.activityResultRegistry
        checkNotNull(registry) { "activityResultRegistry is null" }
        return registry.register(
            key = UUID.randomUUID().toString(),
            contract = RetrieveCodeFromSmsActivityResultContract(codeRegexPattern),
        ) { code ->
            if (code != null) {
                listeners.forEach {
                    it.onCodeReceived(code)
                }
            }
        }
    }

    private fun getBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
                    val extras = intent.extras ?: return
                    val smsRetrieverStatus =
                        BundleCompat.getParcelable<Status>(extras, SmsRetriever.EXTRA_STATUS)

                    when (smsRetrieverStatus?.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val consentIntent = BundleCompat.getParcelable<Intent>(
                                bundle = extras,
                                key = SmsRetriever.EXTRA_CONSENT_INTENT,
                            ) ?: return
                            try {
                                activityResultLauncher?.launch(consentIntent)
                            } catch (e: ActivityNotFoundException) {
                                Timber.tag(TAG).d(e, "Failed to start activity for result")
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        val permission = SmsRetriever.SEND_PERMISSION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                receiver,
                intentFilter,
                permission,
                null,
                Context.RECEIVER_EXPORTED,
            )
        } else {
            context.registerReceiver(receiver, intentFilter, permission, null)
        }
    }

    override fun close() {
        release()
    }

    fun interface Listener {
        fun onCodeReceived(code: String)
    }

    companion object {
        private const val TAG = "SmsCodeRetriever"
    }
}
