package ru.zarina.zarina.ui.common.components.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.minInteractionSize

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .minInteractionSize()
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = UiKitTheme.typographyOld.circle1216,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
