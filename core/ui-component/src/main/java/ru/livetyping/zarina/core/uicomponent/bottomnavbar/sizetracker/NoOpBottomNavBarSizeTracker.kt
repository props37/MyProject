package ru.livetyping.zarina.core.uicomponent.bottomnavbar.sizetracker

import androidx.compose.runtime.State
import androidx.compose.ui.unit.IntSize

public class NoOpBottomNavBarSizeTracker : BottomNavBarSizeTracker {
    override val sizePx: State<IntSize>
        get() = throw NotImplementedError()

    override fun onSizeChanged(sizePx: IntSize) {
        throw NotImplementedError()
    }
}
