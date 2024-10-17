package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.core.uikit.impl.theme.LightUiKitColors

public val LocalUiKitColors: ProvidableCompositionLocal<UiKitColors> =
    staticCompositionLocalOf { LightUiKitColors }
