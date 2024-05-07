package ru.livetyping.zarina.presentation.common.phone

import android.content.Context
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import timber.log.Timber

object PhoneNumberUtilProvider {

    @Volatile
    private var phoneNumberUtil: PhoneNumberUtil? = null

    private val lock = Any()

    fun provide(context: Context): PhoneNumberUtil {
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

                Timber.tag(TAG).v("Initialize PhoneNumberUtil")
                val instance = PhoneNumberUtil.createInstance(context)
                phoneNumberUtil = instance
                instance
            }
        }
    }

    private const val TAG = "PhoneNumberUtilProvider"

    private const val MESSAGE_ALREADY_INITIALIZED =
        "PhoneNumberUtil is already initialized, return it"
}
