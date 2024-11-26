package ru.livetyping.zarina.core.uicommon

import android.content.Context
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import timber.log.Timber

public object PhoneNumberUtilProvider {

    @Volatile
    private var phoneNumberUtil: PhoneNumberUtil? = null

    private val lock = Any()

    public fun provide(context: Context): PhoneNumberUtil {
        val currentPhoneNumberUtil = phoneNumberUtil
        return if (currentPhoneNumberUtil != null) {
            Timber.tag(TAG).v(MESSAGE_ALREADY_INITIALIZED)
            currentPhoneNumberUtil
        } else {
            synchronized(lock) {
                phoneNumberUtil?.let {
                    Timber.tag(TAG).v(MESSAGE_ALREADY_INITIALIZED)
                    return it
                }

                val instance = PhoneNumberUtil.createInstance(context.applicationContext)
                phoneNumberUtil = instance
                Timber.tag(TAG).v("PhoneNumberUtil initialized")
                instance
            }
        }
    }

    private const val MESSAGE_ALREADY_INITIALIZED = "PhoneNumberUtil is already initialized"

    private const val TAG = "PhoneNumberUtilProvider"
}
