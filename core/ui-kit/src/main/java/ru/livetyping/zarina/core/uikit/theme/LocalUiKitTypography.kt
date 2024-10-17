package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalUiKitTypography: ProvidableCompositionLocal<UiKitTypography> =
    staticCompositionLocalOf { UiKitTypography() }
