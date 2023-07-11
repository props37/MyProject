package ru.zarina.zarina.ui.common.behavior.navigationbar

import ru.zarina.zarina.ui.common.base.behavior.Behavior

sealed interface NavigationBarBehavior : Behavior {

    val isAnimated: Boolean

    data class Visible(override val isAnimated: Boolean) : NavigationBarBehavior

    data class Hidden(override val isAnimated: Boolean) : NavigationBarBehavior

}
