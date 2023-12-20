package ru.zarina.zarina.ui.common.rippletheme

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

object DarkRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = LocalContentColor.current,
        lightTheme = true,
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = true,
    )
}

@Composable
fun DarkRippleTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalRippleTheme provides DarkRippleTheme,
        content = content,
    )
}
