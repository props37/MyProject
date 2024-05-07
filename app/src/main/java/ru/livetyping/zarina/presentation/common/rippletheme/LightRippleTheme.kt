package ru.livetyping.zarina.presentation.common.rippletheme

import androidx.compose.material.LocalContentColor
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun LightRippleTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalRippleTheme provides LightRippleTheme,
        content = content,
    )
}

object LightRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = LocalContentColor.current,
        lightTheme = false,
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = false,
    )
}
