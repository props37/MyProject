package ru.zarina.zarina.util.library.shimmer

import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

fun Modifier.shimmerSwitchable(
    shimmer: Shimmer?,
    isEnabled: Boolean = true,
): Modifier = if (isEnabled) then(shimmer(shimmer)) else this
