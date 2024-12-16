package ru.livetyping.zarina.core.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import java.util.Locale

public fun Context.getLocale(): Locale {
    val locales = this.resources.configuration.locales
    return if (!locales.isEmpty) {
        locales[0]
    } else {
        Locale.getDefault()
    }
}

public fun Context.copyTextToClipboard(label: String, text: String) {
    val clipboardManager = this.getSystemService<ClipboardManager>() ?: return
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
}

public fun Context.shareText(text: String) {
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
