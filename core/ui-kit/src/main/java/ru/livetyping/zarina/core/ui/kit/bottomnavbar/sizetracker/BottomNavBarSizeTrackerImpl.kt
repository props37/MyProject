package ru.livetyping.zarina.core.ui.kit.bottomnavbar.sizetracker

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntSize

public class BottomNavBarSizeTrackerImpl : BottomNavBarSizeTracker {
    private val _sizePx = mutableStateOf(IntSize.Zero)
    override val sizePx: State<IntSize> = _sizePx

    override fun onSizeChanged(sizePx: IntSize) {
        _sizePx.value = sizePx
    }
}
