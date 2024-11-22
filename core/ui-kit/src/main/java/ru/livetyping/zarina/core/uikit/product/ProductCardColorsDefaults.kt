package ru.livetyping.zarina.core.uikit.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.uicompose.toComposeColor
import ru.livetyping.zarina.core.uikit.color.ZarinaColorIcon
import ru.livetyping.zarina.core.uikit.product.ProductCardColorsDefaults.MoreTextWidth
import ru.livetyping.zarina.core.uikit.product.ProductCardColorsDefaults.Size
import ru.livetyping.zarina.core.uikit.product.ProductCardColorsDefaults.SpacedBy
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

// TODO: [Medium] Rewrite using FlowRow
@Composable
internal fun ProductCardColors(
    colors: List<ProductColor>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpacedBy),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val colorsFit = remember(maxWidth, colors.size) {
                val colorsAvailableWidth = maxWidth - MoreTextWidth
                (colorsAvailableWidth / (Size + SpacedBy))
                    .toInt()
                    .coerceIn(0, colors.size)
            }
            val colorsLeft = remember(colorsFit, colors.size) {
                (colors.size - colorsFit).coerceIn(0, colors.size)
            }

            for (i in 0 until colorsFit) {
                val color = colors.getOrNull(i)
                if (color != null) {
                    key(color.id.value) {
                        ZarinaColorIcon(color = color.color.toComposeColor() ?: Color.Unspecified)
                    }
                }
            }

            val moreColorsText = if (colorsLeft > 0) "+$colorsLeft" else ""
            Text(
                text = moreColorsText,
                style = UiKitTheme.typography.caption2.regular,
                color = UiKitTheme.colors.text.general.regular.muted,
            )
        }
    }
}

internal object ProductCardColorsDefaults {
    val Size = 8.dp
    val SpacedBy = Size
    val MoreTextWidth = 16.dp
}
