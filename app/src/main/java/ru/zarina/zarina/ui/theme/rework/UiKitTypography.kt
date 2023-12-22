package ru.zarina.zarina.ui.theme.rework

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import ru.zarina.zarina.R
import ru.zarina.zarina.utils.compose.W350

private val CirceBold = Font(
    resId = R.font.circe_bold,
    weight = FontWeight.W700,
)

private val CirceRegular = Font(
    resId = R.font.circe_regular,
    weight = FontWeight.W400,
)

private val CirceLight = Font(
    resId = R.font.circe_light,
    weight = FontWeight.W350,
)

private val CirceFamily = FontFamily(CirceBold, CirceRegular, CirceLight)

// TODO: [High] Remove after updating to Compose 1.6.0
private val PlatformStyle = PlatformTextStyle(includeFontPadding = false)

@Immutable
data class UiKitTypographyReworked(
    val heading1: Heading1 = Heading1(),
    val heading2: Heading2 = Heading2(),
    val heading3: Heading3 = Heading3(),
    val heading4: Heading4 = Heading4(),
    val primaryText: PrimaryText = PrimaryText(),
    val secondaryText: SecondaryText = SecondaryText(),
    val tertiaryText: TertiaryText = TertiaryText(),
    val footnote: Footnote = Footnote(),
    val caption1: Caption1 = Caption1(),
    val caption2: Caption2 = Caption2(),
    val caption3: Caption3 = Caption3(),
) {
    @Immutable
    data class Heading1(
        val bold: TextStyle = TextStyle(
            fontSize = 34.sp,
            lineHeight = 41.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 34.sp,
            lineHeight = 41.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class Heading2(
        val bold: TextStyle = TextStyle(
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class Heading3(
        val bold: TextStyle = TextStyle(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class Heading4(
        val bold: TextStyle = TextStyle(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class PrimaryText(
        val bold: TextStyle = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class SecondaryText(
        val bold: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            platformStyle = PlatformStyle,
        ),
        val strikethrough: TextStyle = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            textDecoration = TextDecoration.LineThrough,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class TertiaryText(
        val bold: TextStyle = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class Footnote(
        val bold: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            platformStyle = PlatformStyle,
        ),
        val strikethrough: TextStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            textDecoration = TextDecoration.LineThrough,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class Caption1(
        val bold: TextStyle = TextStyle(
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            platformStyle = PlatformStyle,
        ),
        val light: TextStyle = TextStyle(
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W350,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class Caption2(
        val bold: TextStyle = TextStyle(
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.12.sp,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.12.sp,
            platformStyle = PlatformStyle,
        ),
    )

    @Immutable
    data class Caption3(
        val bold: TextStyle = TextStyle(
            fontSize = 9.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.16.sp,
            platformStyle = PlatformStyle,
        ),
        val regular: TextStyle = TextStyle(
            fontSize = 9.sp,
            lineHeight = 12.sp,
            fontFamily = CirceFamily,
            fontWeight = FontWeight.W400,
            letterSpacing = 0.16.sp,
            platformStyle = PlatformStyle,
        ),
    )
}

val LocalUiKitTypographyReworked = staticCompositionLocalOf { UiKitTypographyReworked() }
