package ru.zarina.zarina.ui.common.behavior.bottomnavbar

import ru.zarina.zarina.ui.common.base.behavior.Behavior

sealed class BottomNavBarBehavior : Behavior {
    abstract val isAnimated: Boolean

    data class Visible(override val isAnimated: Boolean) : BottomNavBarBehavior()

    data class Hidden(override val isAnimated: Boolean) : BottomNavBarBehavior()
}
