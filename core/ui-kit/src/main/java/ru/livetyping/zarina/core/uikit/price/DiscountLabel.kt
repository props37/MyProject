package ru.livetyping.zarina.core.uikit.price

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import ru.livetyping.zarina.core.uikit.text.withZarinaBrackets
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import java.math.BigDecimal

@Composable
public fun DiscountLabel(
    discountPercent: BigDecimal,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val hazeBackgroundColor = UiKitTheme2.colors.white
    val hazeTint = rememberHazeTint(hazeBackgroundColor)

    Box(
        modifier = modifier
            .hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    backgroundColor = hazeBackgroundColor,
                    blurRadius = 4.dp,
                    tint = hazeTint,
                    noiseFactor = 0f,
                )
            )
            .background(Color.Transparent)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = "-$discountPercent%".withZarinaBrackets(),
            style = UiKitTheme2.typography.caption2,
            color = UiKitTheme2.colors.mainBlack,
        )
    }
}

@Composable
private fun rememberHazeTint(backgroundColor: Color): HazeTint {
    return remember(backgroundColor) {
        val color = backgroundColor.copy(alpha = 0.7f)
        HazeTint(color)
    }
}

public object DiscountLabelDefault {

}
