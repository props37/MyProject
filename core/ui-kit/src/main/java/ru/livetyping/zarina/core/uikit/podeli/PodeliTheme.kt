package ru.livetyping.zarina.core.uikit.podeli

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.livetyping.zarina.core.uikit.R

internal object PodeliTheme {
    internal val ContentColor = Color(0xFF232323)

    internal val AccentColor = Color(0xFFFF1A1A)

    internal val CounterColor = Color(0xFFA7ABAC)

    internal val PagerIndicatorInactiveColor = Color(0xFFD9D9D9)

    internal val HelperTextColor = Color(0xFF7F7E82)

    internal val FootnoteBackgroundColor = Color(0xFFF5F5F5)

    internal val DividerColor = Color(0xFFCCCCCC)

    internal val ManropeFontFamily = FontFamily(
        ManropeRegularFont,
        ManropeMediumFont,
        ManropeBoldFont,
    )

    private val ManropeRegularFont: Font
        get() = Font(
            resId = R.font.manrope_regular,
            weight = FontWeight.W400,
        )

    private val ManropeMediumFont: Font
        get() = Font(
            resId = R.font.manrope_medium,
            weight = FontWeight.W500,
        )

    private val ManropeBoldFont: Font
        get() = Font(
            resId = R.font.manrope_bold,
            weight = FontWeight.W700,
        )
}
