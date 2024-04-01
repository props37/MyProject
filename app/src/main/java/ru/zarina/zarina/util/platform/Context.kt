package ru.zarina.zarina.util.platform

import android.content.Context
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
