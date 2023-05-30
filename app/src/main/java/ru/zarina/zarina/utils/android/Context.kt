package ru.zarina.zarina.utils.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber

fun Context.share(content: String) {
    val contentIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val chooserIntent = Intent.createChooser(contentIntent, null)
    startActivity(chooserIntent)
}

fun Context.openBrowser(url: String): Boolean {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val isActivityStarted = this.tryStartActivity(intent)
    if (!isActivityStarted) {
        Timber.tag("Context.openBrowser").w("Activity that can open URL is not found")
    }
    return isActivityStarted
}

fun Context.tryStartActivity(intent: Intent): Boolean {
    return if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        true
    } else {
        false
    }
}
