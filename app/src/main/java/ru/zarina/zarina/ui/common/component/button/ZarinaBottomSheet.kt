package ru.zarina.zarina.ui.common.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaBottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 4.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp,
    )

    Box(
        modifier = modifier
            .background(
                color = UiKitTheme.colorsReworked.background.general.regular.background,
                shape = shape,
            )
            .shadow(elevation = 4.dp, shape = shape),
    ) {
        content()
    }
}
