package ru.livetyping.zarina.ui.common.components.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun Header(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = UiKitTheme.typographyOld.circle1718bold,
        textAlign = TextAlign.Start,
        modifier = modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}
