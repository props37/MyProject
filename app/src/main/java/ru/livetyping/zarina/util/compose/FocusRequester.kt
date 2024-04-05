package ru.livetyping.zarina.util.compose

import androidx.compose.ui.focus.FocusRequester
import timber.log.Timber

fun FocusRequester.tryRequestFocus() {
    try {
        this.requestFocus()
    } catch (e: IllegalStateException) {
        Timber.e(e, "Failed to request focus as FocusRequester is not initialized")
    }
}
