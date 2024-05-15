package ru.livetyping.zarina.presentation.common.util.domain

import androidx.compose.runtime.Stable
import timber.log.Timber
import android.graphics.Color as PlatformColor
import androidx.compose.ui.graphics.Color as ComposeColor
import ru.livetyping.zarina.domain.common.Color as DomainColor

@Stable
fun DomainColor.toComposeColor(): ComposeColor? {
    return try {
        val intCode = PlatformColor.parseColor(this.value)
        ComposeColor(intCode)
    } catch (e: Exception) {
        Timber.e(e, "Failed to parse domain color: $this")
        null
    }
}
