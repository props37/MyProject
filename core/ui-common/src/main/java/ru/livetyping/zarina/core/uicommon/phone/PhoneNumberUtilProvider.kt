package ru.livetyping.zarina.core.uicommon.phone

import android.content.Context
import android.util.Log
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

public object PhoneNumberUtilProvider {

    @Volatile
    private var phoneNumberUtil: PhoneNumberUtil? = null

    private val lock = Any()

    public fun provide(context: Context): PhoneNumberUtil {
        val currentPhoneNumberUtil = phoneNumberUtil
        return if (currentPhoneNumberUtil != null) {
            Log.v(TAG, MESSAGE_ALREADY_INITIALIZED)
            currentPhoneNumberUtil
        } else {
            synchronized(lock) {
                phoneNumberUtil?.let {
                    Log.v(TAG, MESSAGE_ALREADY_INITIALIZED)
                    return it
                }

                val instance = PhoneNumberUtil.createInstance(context.applicationContext)
                phoneNumberUtil = instance
                Log.v(TAG, "PhoneNumberUtil initialized")
                instance
            }
        }
    }

    private const val MESSAGE_ALREADY_INITIALIZED = "PhoneNumberUtil is already initialized"

    private const val TAG = "PhoneNumberUtilProvider"
}
