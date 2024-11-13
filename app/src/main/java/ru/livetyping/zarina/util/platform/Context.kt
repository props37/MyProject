package ru.livetyping.zarina.util.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.getSystemService
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

fun Context.copyTextToClipboard(label: String, text: String) {
    val clipboardManager = this.getSystemService<ClipboardManager>() ?: return
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
}

private const val MIME_TYPE_TEXT_PLAIN = "text/plain"
