package ru.livetyping.zarina.ui.common.behavior.systembars

import ru.livetyping.zarina.base.behavior.Behavior

data class SystemBarsBehavior(
    val isStatusBarContentLight: Boolean,
    val isNavigationBarContentLight: Boolean,
) : Behavior
