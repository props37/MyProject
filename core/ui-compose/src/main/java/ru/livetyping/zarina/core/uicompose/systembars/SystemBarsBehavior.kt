package ru.livetyping.zarina.core.uicompose.systembars

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.uicommon.behavior.Behavior

@Immutable
public data class SystemBarsBehavior(
    val isStatusBarContentLight: Boolean,
    val isNavigationBarContentLight: Boolean,
) : Behavior
