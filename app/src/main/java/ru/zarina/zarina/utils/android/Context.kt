package ru.zarina.zarina.utils.android

import android.content.Context
import android.content.Intent

fun Context.share(content: String) {
    val contentIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val chooserIntent = Intent.createChooser(contentIntent, null)
    startActivity(chooserIntent)
}
