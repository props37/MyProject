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
    val primaryButtonBorder: Color = Mineshaft,
    val primaryBorderColor: Color = Mercury,
    val primaryAccentColor: Color = SolidPink,
    val secondaryButtonBackground: Color = Color.White,
    val secondaryButtonForeground: Color = Color.Black,
    val secondaryButtonBorder: Color = Mineshaft,
    val snackbarBackground: Color = Color.White,
    val snackbarForeground: Color = Color.Black,
    val hint: Color = RollingStone,
    val listDivider: Color = Mercury,
    val listItemSubtitle: Color = RollingStone,
    val pagerDot: Color = Color.White,
    val price: Color = SolidPink,
    val retiredPrice: Color = RollingStone,
    val discountBadgeBackground: Color = Mineshaft,
    val discountBadgeForeground: Color = Color.White,
    val colorPickerCircleBorder: Color = Mercury,
    val colorPickerCircleSelectionBorder: Color = Color.Black,
    val modalLoaderProgress: Color = Color.White,
    val modalLoaderOverlay: Color = Color.Black.copy(0.4f),
)


val LocalUiKitColors = staticCompositionLocalOf { UiKitColors() }

@Immutable
data class UiKitTypography(
    val button: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Fonts.Families.Circle
    ),
    val snackbar: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val onboardingHeader: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val onboardingBody: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val input: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val hint: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val screenToolbarTitle: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val screenToolbarSubtitle: TextStyle = TextStyle(
        fontSize = 8.sp,
        lineHeight = 11.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val listHeaderItem: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val listRegularItem: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val listRegularItemSubtitle: TextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 16.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val errorPlaceholderTitle: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Fonts.Families.Circle
    ),
    val errorPlaceholderBody: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productPrice: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val discountBadge: TextStyle = TextStyle(
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productDetailsHeader: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productDetailsContent: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productCardName: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productCardPrice: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 14.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productCardTag: TextStyle = TextStyle(
        fontSize = 8.sp,
        lineHeight = 11.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productCardColorCount: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val productSectionHeader: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontFamily = Fonts.Families.Circle
    ),
    val deliveryInformationHeader: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Fonts.Families.Circle
    ),
    val deliveryInformationBody: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = Fonts.Families.Circle
    ),
)

val LocalUiKitTypography = staticCompositionLocalOf { UiKitTypography() }
