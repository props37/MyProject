package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.toComposeColor
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.text.withZarinaBrackets
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuItem

@Composable
internal fun MenuItemBasic(
    item: MenuItem.Basic,
    onClick: (MenuItem.Basic) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (item.isHighlighted) {
            UiKitTheme2.colors.lightGray
        } else {
            UiKitTheme2.colors.white
        },
    )
    val startPadding = if (item.addStartPadding) 48.dp else 16.dp

    ZarinaItem(
        onClick = { onClick(item) },
        backgroundColor = backgroundColor,
        contentPadding = PaddingValues(
            start = startPadding,
            top = 4.dp,
            end = 16.dp,
            bottom = 4.dp,
        ),
        modifier = modifier,
    ) {
        val text = if (item.addBrackets) {
            item.item.title.uppercase().withZarinaBrackets()
        } else {
            item.item.title.uppercase()
        }

        val color = item.item.color?.toComposeColor() ?: Color.Unspecified

        Text(
            text = text,
            style = UiKitTheme2.typography.body,
            color = color,
        )

        val label = item.item.label
        if (label != null) {
            Text(
                text = label.uppercase().withZarinaBrackets(),
                style = UiKitTheme2.typography.caption2,
                color = color,
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 8.dp),
            )
        }
    }
}
