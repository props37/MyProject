package ru.zarina.zarina.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object UiKitTheme {
    val colors: UiKitColors
        @Composable
        get() = LocalUiKitColors.current

    val typography: UiKitTypography
        @Composable
        get() = LocalUiKitTypography.current
}

@Immutable
data class UiKitColors(
    val primaryContentColor: Color = Color.Black,
    val screenBackground: Color = Color.White,
    val primaryButtonBackground: Color = Mineshaft,
    val primaryButtonForeground: Color = Color.White,
    val primaryButtonDisabledBackground: Color = Aluminium,
    val primaryButtonDisabledForeground: Color = Color.White,
    val primaryButtonBorder: Color = Color.Transparent,
    val primaryBorderColor: Color = Mercury,
    val primaryAccentColor: Color = SolidPink,
    val secondaryButtonBackground: Color = Color.White,
    val secondaryButtonForeground: Color = Color.Black,
    val secondaryButtonDisabledBackground: Color = Color.White,
    val secondaryButtonDisabledForeground: Color = Aluminium,
    val secondaryButtonBorder: Color = Mineshaft,
    val snackbarBackground: Color = Color.White,
    val snackbarForeground: Color = Color.Black,
    val disabled: Color = RollingStone,
    val hint: Color = RollingStone,
    val error: Color = Monza,
    val listDivider: Color = Mercury,
    val listItemSubtitle: Color = RollingStone,
    val pagerDot: Color = Color.White,
    val price: Color = SolidPink,
    val retiredPrice: Color = RollingStone,
    val productBadgeBackground: Color = Mineshaft,
    val productBadgeForeground: Color = Color.White,
    val colorPickerCircleBorder: Color = Mercury,
    val colorPickerCircleSelectionBorder: Color = Color.Black,
    val modalLoaderProgress: Color = Color.White,
    val modalLoaderOverlay: Color = Color.Black.copy(0.4f),
    val inactiveOverlay: Color = Color.White.copy(0.6f),
)


val LocalUiKitColors = staticCompositionLocalOf { UiKitColors() }

@Immutable
data class UiKitTypography(
    val circle1720bold: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1420: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle2028: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1718: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1518: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1518bold: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle,
        fontWeight = FontWeight.Bold
    ),
    val circle811: TextStyle = TextStyle(
        fontSize = 8.sp,
        lineHeight = 11.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle2026: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1618: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1316: TextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 16.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1012: TextStyle = TextStyle(
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1614: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 14.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val circle1718bold: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle,
        fontWeight = FontWeight.Bold,
    ),
)

val LocalUiKitTypography = staticCompositionLocalOf { UiKitTypography() }
