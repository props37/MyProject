package ru.livetyping.zarina.core.uicompose

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import timber.log.Timber
import ru.livetyping.zarina.core.domain.model.common.Color as ColorDomain

@Stable
public fun ColorDomain.toComposeColor(): Color? {
    return try {
        val intCode = this.value.toColorInt()
        Color(intCode)
    } catch (e: Exception) {
        Timber.tag(Tag).e(e, "Failed to convert domain color $this to Compose color")
        null
    }
}

private const val Tag = "Color"
