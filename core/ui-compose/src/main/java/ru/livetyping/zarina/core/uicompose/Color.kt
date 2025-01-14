package ru.livetyping.zarina.core.uicompose

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import timber.log.Timber
import ru.livetyping.zarina.core.domain.model.common.Color as ColorDomain

@Stable
public fun ColorDomain.toComposeColor(): Color? {
    return try {
        val intCode = android.graphics.Color.parseColor(this.value)
        Color(intCode)
    } catch (e: Exception) {
        Timber.tag(Tag).e(e, "Failed to convert domain color $this to Compose color")
        null
    }
}

private const val Tag = "Color"
