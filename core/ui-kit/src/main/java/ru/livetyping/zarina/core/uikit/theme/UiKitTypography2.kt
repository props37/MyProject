package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.livetyping.zarina.core.uikit.util.W350

// TODO: [Top] Rename after full migration

public data class UiKitTypography2(
    val h1: TextStyle = TextStyle(
        fontSize = 25.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W350,
    ),
    val h2: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W350,
    ),
    val h2Regular: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W400,
    ),
    val h3: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W350,
    ),
    val h4: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W400,
    ),
    val body: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W400,
    ),
    val bodyBold: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W700,
    ),
    val body2: TextStyle = TextStyle(
        fontSize = 10.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W400,
    ),
    val body2Bold: TextStyle = TextStyle(
        fontSize = 10.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W700,
    ),
    val caption: TextStyle = TextStyle(
        fontSize = 9.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W400,
    ),
    val caption2: TextStyle = TextStyle(
        fontSize = 8.sp,
        fontFamily = CirceFamily,
        fontWeight = FontWeight.W400,
    ),
)
