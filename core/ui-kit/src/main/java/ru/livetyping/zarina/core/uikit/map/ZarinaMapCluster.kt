package ru.livetyping.zarina.core.uikit.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
public fun ZarinaMapCluster(
    clusterSize: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .defaultMinSize(Size, Size)
            .background(
                color = UiKitTheme2.colors.white,
                shape = CircleShape,
            )
            .border(
                width = 1.dp,
                color = UiKitTheme2.colors.mainBlack,
                shape = CircleShape,
            ),
    ) {
        val text = if (clusterSize <= MaxClusterSize) {
            clusterSize.toString()
        } else {
            "$MaxClusterSize+"
        }
        Text(
            text = text.uppercase(),
            style = UiKitTheme2.typography.body,
            color = UiKitTheme2.colors.mainBlack,
            maxLines = 1,
        )
    }
}

private val Size: Dp get() = 52.dp

private const val MaxClusterSize = 99
