package ru.livetyping.zarina.util.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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

fun Context.openApplicationSettings(
    action: String = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
) {
    val packageName = packageName ?: return
    val intent = Intent(action).apply {
        data = Uri.fromParts("package", packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    this.startActivity(intent)
}
