package ru.livetyping.zarina.core.analytics.impl.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.core.analytics.AppMetrica

public val LocalAppMetrica: ProvidableCompositionLocal<AppMetrica?> =
    staticCompositionLocalOf { null }
