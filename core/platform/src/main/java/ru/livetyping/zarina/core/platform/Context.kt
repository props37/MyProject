package ru.livetyping.zarina.core.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
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

public fun Context.dialPhoneNumber(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

public fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

private const val MIME_TYPE_TEXT_PLAIN = "text/plain"
