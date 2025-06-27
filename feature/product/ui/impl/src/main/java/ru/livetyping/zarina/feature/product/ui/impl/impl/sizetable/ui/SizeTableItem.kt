package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
internal fun SizeTableItem(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = UiKitTheme2.typography.body

    ZarinaItem(
        startContent = {
            Text(
                text = name.uppercase(),
                style = textStyle,
            )
        },
        endContent = {
            Text(
                text = value.uppercase(),
                style = textStyle,
            )
        },
        modifier = modifier.heightIn(min = 56.dp),
    )
}
