package ru.livetyping.zarina.core.uikit.blur

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlurDefaults.BlurInputScale
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlurDefaults.BlurRadius
import ru.livetyping.zarina.core.uikit.blur.StatusBarBlurDefaults.FallbackScrimAlpha
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@OptIn(ExperimentalHazeApi::class)
@Composable
public fun StatusBarBlur(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = UiKitTheme2.colors.white
    val hazeTint = remember(backgroundColor, hazeState.blurEnabled) {
        if (hazeState.blurEnabled) {
            HazeTint(Color.Transparent)
        } else {
            HazeTint(backgroundColor.copy(alpha = FallbackScrimAlpha))
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.safeDrawing)
            .hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    backgroundColor = backgroundColor,
                    blurRadius = BlurRadius,
                    tint = hazeTint,
                    noiseFactor = 0f,
                ),
            ) {
                inputScale = HazeInputScale.Fixed(BlurInputScale)
                progressive = HazeProgressive.verticalGradient(
                    startIntensity = 1f,
                    endIntensity = 0f,
                    preferPerformance = true,
                )
            },
    )
}

public object StatusBarBlurDefaults {
    internal const val FallbackScrimAlpha = 0.74f
    internal val BlurRadius: Dp get() = 10.dp
    internal const val BlurInputScale = 0.66f

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public fun isStatusBarBlurEnabled(): Boolean {
        // Progressive blur is more performant on Android 34+ according to https://chrisbanes.github.io/haze/latest/performance/
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
    }
}
