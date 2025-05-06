package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

// TODO: [Top] Remove after full migration

public val LocalUiKitTypography: ProvidableCompositionLocal<UiKitTypography> =
    staticCompositionLocalOf { UiKitTypography() }
