package ru.livetyping.zarina.core.uicommon

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

public fun Context.openUrlInCustomTabs(url: String) {
    val intent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
    intent.launchUrl(this, url.toUri())
}
