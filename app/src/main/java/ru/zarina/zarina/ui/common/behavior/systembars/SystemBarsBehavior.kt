package ru.zarina.zarina.ui.common.behavior.systembars

import ru.zarina.zarina.base.behavior.Behavior

data class SystemBarsBehavior(
    val isStatusBarContentLight: Boolean,
    val isNavigationBarContentLight: Boolean,
) : Behavior
