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

fun Context.shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = MIME_TYPE_TEXT_PLAIN
    }
    val shareIntent = Intent.createChooser(intent, null)
    if (shareIntent.resolveActivity(packageManager) != null) {
        startActivity(shareIntent)
    }
}

private const val MIME_TYPE_TEXT_PLAIN = "text/plain"
