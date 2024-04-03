package ru.livetyping.zarina.ui.common.utils.domain

import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.domain.old.Color as ZarinaColor


fun ZarinaColor.toColorOr(fallback: Color): Color {
    runCatching { android.graphics.Color.parseColor(this.code.value) }
        .onSuccess { return Color(it) }
        .onFailure { return fallback }
    return fallback
}
