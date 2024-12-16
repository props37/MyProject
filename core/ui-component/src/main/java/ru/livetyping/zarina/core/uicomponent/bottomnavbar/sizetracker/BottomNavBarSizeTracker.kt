package ru.livetyping.zarina.core.uicomponent.bottomnavbar.sizetracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.IntSize

@Suppress("ComposeCompositionLocalUsage")
public val LocalBottomNavBarSizeTracker: ProvidableCompositionLocal<BottomNavBarSizeTracker> =
    staticCompositionLocalOf { NoOpBottomNavBarSizeTracker() }

@Composable
public fun rememberBottomNavBarSizeTracker(): BottomNavBarSizeTracker {
    return remember { BottomNavBarSizeTrackerImpl() }
}

public interface BottomNavBarSizeTracker {
    public val sizePx: State<IntSize>

    public fun onSizeChanged(sizePx: IntSize)
}
