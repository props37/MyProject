package ru.livetyping.zarina.core.ui.kit.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import ru.livetyping.zarina.core.ui.kit.R
import ru.livetyping.zarina.core.ui.kit.impl.theme.W350

private val CirceBold: Font
    get() = Font(
        resId = R.font.circe_bold,
        weight = FontWeight.W700,
    )

private val CirceRegular: Font
    get() = Font(
        resId = R.font.circe_regular,
        weight = FontWeight.W400,
    )

private val CirceLight: Font
    get() = Font(
        resId = R.font.circe_light,
        weight = FontWeight.W350,
    )

private val CirceFamily = FontFamily(CirceBold, CirceRegular, CirceLight)

public data class UiKitTypography(
    val heading1: Heading1 = Heading1(),
    val heading2: Heading2 = Heading2(),
    val heading3: Heading3 = Heading3(),
    val heading4: Heading4 = Heading4(),
    val primary: Primary = Primary(),
    val secondary: Secondary = Secondary(),
    val tertiary: Tertiary = Tertiary(),
    val footnote: Footnote = Footnote(),
    val caption1: Caption1 = Caption1(),
    val caption2: Caption2 = Caption2(),
    val caption3: Caption3 = Caption3(),
) {
    public data class Heading1(
        val bold: TextStyle = TextStyle(
            fontSize = 34.sp,
            lineHeight = 41.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 34.sp,
            lineHeight = 41.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
    )

    public data class Heading2(
        val bold: TextStyle = TextStyle(
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
    )

    public data class Heading3(
        val bold: TextStyle = TextStyle(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
    )

    public data class Heading4(
        val bold: TextStyle = TextStyle(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
    )

    public data class Primary(
        val bold: TextStyle = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
        ),
    )

    public data class Secondary(
        val bold: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
        ),
        val strikethrough: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            textDecoration = TextDecoration.LineThrough,
        ),
    )

    public data class Tertiary(
        val bold: TextStyle = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
        ),
    )

    public data class Footnote(
        val bold: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
        ),
        val strikethrough: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            textDecoration = TextDecoration.LineThrough,
        ),
    )

    public data class Caption1(
        val bold: TextStyle = TextStyle(
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
        ),
    )

    public data class Caption2(
        val bold: TextStyle = TextStyle(
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.12.sp,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.12.sp,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            letterSpacing = 0.12.sp,
        ),
    )

    public data class Caption3(
        val bold: TextStyle = TextStyle(
            fontSize = 9.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.16.sp,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 9.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.16.sp,
        ),
    )
}
