package ru.livetyping.zarina.core.googleplayservices.impl.sms

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.livetyping.zarina.core.googleplayservices.impl.BundleCompat
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever.Listener
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

internal class SmsCodeRetrieverImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : SmsCodeRetriever {
    private val activityResultRegistryRef = AtomicReference<ActivityResultRegistry?>(null)

    private val listeners = mutableListOf<Listener>()

    private val smsRetrieverClient by lazy {
        SmsRetriever.getClient(context)
    }

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private val receiver by lazy {
        getBroadcastReceiver()
    }

    override fun start(sender: String, codeRegexPattern: String) {
        stop()
        Timber.tag(TAG).v("Start")

        smsRetrieverClient.startSmsUserConsent(sender)
        activityResultLauncher = getActivityResultLauncher(codeRegexPattern)
        registerReceiver()
    }

    override fun stop() {
        activityResultLauncher?.unregister()
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            Timber.tag(TAG).w("Failed to unregister BroadcastReceiver")
        }
        Timber.tag(TAG).v("SmsCodeRetriever stopped")
    }

    override fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun setActivityResultRegistry(registry: ActivityResultRegistry) {
        activityResultRegistryRef.set(registry)
        Timber.tag(TAG).v("ActivityResultRegistry set")
    }

    override fun unsetActivityResultRegistry(registry: ActivityResultRegistry) {
        val unset = activityResultRegistryRef.compareAndSet(
            /* expectedValue = */ registry,
            /* newValue = */ null,
        )
        Timber.tag(TAG).v("ActivityResultRegistry unset: $unset")
    }

    override fun release() {
        stop()
        activityResultRegistryRef.set(null)
        listeners.clear()
        Timber.tag(TAG).v("Released")
    }

    private fun getActivityResultLauncher(codeRegexPattern: String): ActivityResultLauncher<Intent> {
        val registry = requireActivityResultRegistry()
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
                try {
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
                                    Timber.tag(TAG).e(e, "Failed to start activity for result")
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Timber.tag(TAG).e(e)
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
                /* receiver = */ receiver,
                /* filter = */ intentFilter,
                /* broadcastPermission = */ permission,
                /* scheduler = */ null,
                /* flags = */ Context.RECEIVER_EXPORTED,
            )
        } else {
            context.registerReceiver(
                /* receiver = */ receiver,
                /* filter = */ intentFilter,
                /* broadcastPermission = */ permission,
                /* scheduler = */ null,
            )
        }
    }

    private fun requireActivityResultRegistry(): ActivityResultRegistry {
        val registry = activityResultRegistryRef.get()
        checkNotNull(registry) {
            "ActivityResultRegistry can not be null. Did you forget to call setActivityResultRegistry?"
        }
        return registry
    }

    private companion object {
        private const val TAG = "SmsCodeRetrieverImpl"
    }
}