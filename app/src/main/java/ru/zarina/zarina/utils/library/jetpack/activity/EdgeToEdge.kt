package ru.zarina.zarina.utils.library.jetpack.activity

import android.graphics.Color
import androidx.activity.SystemBarStyle

// Source: androidx.activity.EdgeToEdge.kt
val SystemBarStyle.Companion.DefaultLightScrim: Int
    get() = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

// Source: androidx.activity.EdgeToEdge.kt
val SystemBarStyle.Companion.DefaultDarkScrim: Int
    get() = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
