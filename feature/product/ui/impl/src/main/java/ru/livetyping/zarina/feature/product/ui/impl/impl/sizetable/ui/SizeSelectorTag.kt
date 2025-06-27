package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.tag.ZarinaTag
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
internal fun SizeSelectorTag(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    ZarinaTag(
        onClick = onClick,
        isSelected = isSelected,
        modifier = modifier.sizeIn(minWidth = 64.dp, minHeight = 40.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = UiKitTheme2.typography.body,
        )
    }
}
