package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.MenuItem

// TODO: [Top] Add labels
// TODO: [Top] Add colors
// TODO: [Top] Add brackets
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
        Text(
            text = item.item.title.uppercase(),
            style = UiKitTheme2.typography.body,
        )
    }
}
