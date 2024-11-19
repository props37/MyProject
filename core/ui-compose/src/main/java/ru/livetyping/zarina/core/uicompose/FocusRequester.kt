package ru.livetyping.zarina.core.uicompose

import androidx.compose.ui.focus.FocusRequester
import timber.log.Timber

public fun FocusRequester.tryRequestFocus() {
    try {
        this.requestFocus()
    } catch (e: IllegalStateException) {
        Timber.tag(TAG).e(e, "Failed to request focus as FocusRequester is not initialized")
    }
}

private const val TAG = "FocusRequester"
