package ru.zarina.zarina.ui.bottomnavbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.IntSize

val LocalBottomNavBarSizeTracker = staticCompositionLocalOf<BottomNavBarSizeTracker> {
    NoOpBottomNavBarSizeTracker()
}

@Composable
fun rememberBottomNavBarSizeTracker(): BottomNavBarSizeTracker {
    return remember { BottomNavBarSizeTrackerImpl() }
}

interface BottomNavBarSizeTracker {
    val sizePx: State<IntSize>

    fun onSizeChanged(sizePx: IntSize)
}

class BottomNavBarSizeTrackerImpl : BottomNavBarSizeTracker {
    private val _sizePx = mutableStateOf(IntSize.Zero)
    override val sizePx: State<IntSize> = _sizePx

    override fun onSizeChanged(sizePx: IntSize) {
        _sizePx.value = sizePx
    }
}

class NoOpBottomNavBarSizeTracker(
    private val throwExceptions: Boolean = true,
) : BottomNavBarSizeTracker {
    override val sizePx: State<IntSize>
        get() = if (throwExceptions) {
            throw NotImplementedError()
        } else {
            mutableStateOf(IntSize.Zero)
        }

    override fun onSizeChanged(sizePx: IntSize) {
        if (throwExceptions) throw NotImplementedError()
    }
}
