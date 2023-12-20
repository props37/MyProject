package ru.zarina.zarina.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.zarina.zarina.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

object Fonts {
    val CircleRegular = Font(
        resId = R.font.circe_regular,
    )

    val CircleBold = Font(
        resId = R.font.circe_bold,
        weight = FontWeight.Bold,
    )

    object Families {
        val Circle = FontFamily(
            CircleRegular,
            CircleBold
        )
    }
}
