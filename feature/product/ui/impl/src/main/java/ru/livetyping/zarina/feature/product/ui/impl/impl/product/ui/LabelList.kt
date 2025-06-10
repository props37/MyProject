package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uikit.text.withZarinaBrackets
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
internal fun LabelList(
    labels: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        labels.forEach { label ->
            Label(label)
        }
    }
}

@Composable
private fun Label(
    label: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label.withZarinaBrackets().uppercase(),
        style = UiKitTheme2.typography.caption2,
        color = UiKitTheme2.colors.mainBlack,
        modifier = modifier
            .background(
                color = UiKitTheme2.colors.backgroundBeige,
                shape = RoundedCornerShape(1.dp),
            )
            .padding(horizontal = 4.dp, vertical = 1.dp),
    )
}
