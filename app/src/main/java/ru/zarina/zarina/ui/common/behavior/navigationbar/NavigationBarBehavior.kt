package ru.zarina.zarina.ui.common.behavior.navigationbar

import ru.zarina.zarina.base.behavior.Behavior

@Deprecated(
    message = "Use BottomNavBarBehavior instead.",
    replaceWith = ReplaceWith(
        expression = "BottomNavBarBehavior",
        "ru.zarina.zarina.ui.common.behavior.bottomnavbar",
    )
)
sealed interface NavigationBarBehavior : Behavior {

    val isAnimated: Boolean

    data class Visible(override val isAnimated: Boolean) : NavigationBarBehavior

    data class Hidden(override val isAnimated: Boolean) : NavigationBarBehavior

    companion object {
        val DEFAULT
            get() = Hidden(isAnimated = false)
    }

}
