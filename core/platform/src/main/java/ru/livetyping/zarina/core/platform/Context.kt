package ru.livetyping.zarina.core.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService

public fun Context.copyTextToClipboard(label: String, text: String) {
    val clipboardManager = this.getSystemService<ClipboardManager>() ?: return
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
}
