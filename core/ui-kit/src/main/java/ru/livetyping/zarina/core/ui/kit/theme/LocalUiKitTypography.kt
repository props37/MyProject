package ru.livetyping.zarina.core.ui.kit.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalUiKitTypography: ProvidableCompositionLocal<UiKitTypography> =
    staticCompositionLocalOf { UiKitTypography() }
