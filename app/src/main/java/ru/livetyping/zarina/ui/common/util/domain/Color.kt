package ru.livetyping.zarina.ui.common.util.domain

import androidx.compose.runtime.Stable
import android.graphics.Color as PlatformColor
import androidx.compose.ui.graphics.Color as ComposeColor
import ru.livetyping.zarina.domain.common.Color as DomainColor

@Stable
fun DomainColor.toComposeColor(): ComposeColor {
    val intCode = PlatformColor.parseColor(this.value)
    return ComposeColor(intCode)
}
