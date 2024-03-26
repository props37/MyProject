package ru.zarina.zarina.ui.common.behavior.bottomnavbar

import androidx.compose.runtime.Stable
import ru.zarina.zarina.base.behavior.Behavior

@Stable
sealed class BottomNavBarBehavior : Behavior {
    abstract val isAnimated: Boolean

    data class Visible(override val isAnimated: Boolean) : BottomNavBarBehavior()

    data class Hidden(override val isAnimated: Boolean) : BottomNavBarBehavior()
}
