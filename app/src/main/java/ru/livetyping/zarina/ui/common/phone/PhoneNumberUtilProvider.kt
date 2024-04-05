package ru.livetyping.zarina.ui.common.phone

import android.content.Context
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import timber.log.Timber

object PhoneNumberUtilProvider {
    private const val TAG = "PhoneNumberUtilProvider"

    private var phoneNumberUtil: PhoneNumberUtil? = null

    @Synchronized
    fun provide(context: Context): PhoneNumberUtil {
        phoneNumberUtil?.let {
            Timber.tag(TAG).v("PhoneNumberUtil is already initialized, return it")
            return it
        }

        Timber.tag(TAG).v("Initialize PhoneNumberUtil")
        val instance = PhoneNumberUtil.createInstance(context)
        phoneNumberUtil = instance
        return instance
    }
}
