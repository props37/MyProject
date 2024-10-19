package ru.livetyping.zarina.core.uikit.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
public fun Modifier.shimmerToggleable(
    shimmer: Shimmer,
    isEnabled: Boolean = true,
): Modifier = if (isEnabled) this.shimmer(shimmer) else this
