package ru.livetyping.zarina.core.googleplayservices.impl

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

internal object BundleCompat {
    inline fun <reified T : Parcelable> getParcelable(bundle: Bundle, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            return bundle.getParcelable(key)
        }
    }
}
