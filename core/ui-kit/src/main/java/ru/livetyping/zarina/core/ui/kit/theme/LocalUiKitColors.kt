package ru.livetyping.zarina.core.ui.kit.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import ru.livetyping.zarina.core.ui.kit.impl.theme.LightUiKitColors

public val LocalUiKitColors: ProvidableCompositionLocal<UiKitColors> =
    staticCompositionLocalOf { LightUiKitColors }
