package ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior

import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.uicommon.behavior.Behavior

@Stable
public sealed class BottomNavBarBehavior : Behavior {
    public abstract val isAnimated: Boolean

    public data class Visible(override val isAnimated: Boolean) : BottomNavBarBehavior()

    public data class Hidden(override val isAnimated: Boolean) : BottomNavBarBehavior()
}
