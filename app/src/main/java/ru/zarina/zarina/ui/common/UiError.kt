package ru.zarina.zarina.ui.common

import java.io.IOException

enum class UiError {
    NETWORK,
    UNKNOWN;

    companion object {
        fun fromThrowable(throwable: Throwable): UiError = when (throwable) {
            is IOException -> NETWORK
            else -> UNKNOWN
        }
    }
}
