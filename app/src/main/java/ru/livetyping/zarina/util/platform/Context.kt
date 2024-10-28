package ru.livetyping.zarina.util.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.Locale

val Context.locale: Locale
    get() {
        val locales = this.resources.configuration.locales
        return if (!locales.isEmpty) {
            locales[0]
        } else {
            Locale.getDefault()
        }
    }

fun Context.dialPhoneNumber(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}
