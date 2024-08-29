package ru.livetyping.zarina.util.library.shimmer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun Modifier.shimmerToggleable(
    shimmer: Shimmer,
    isEnabled: Boolean = true,
): Modifier = if (isEnabled) this.shimmer(shimmer) else this
