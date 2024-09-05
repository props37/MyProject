package ru.livetyping.zarina.presentation.common.component.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Suppress("ConstPropertyName")
object MapDefaults {

    @Composable
    fun Cluster(
        clusterSize: Int,
        modifier: Modifier = Modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(52.dp)
                .background(
                    color = UiKitTheme.colors.background.general.regular.default,
                    shape = CircleShape,
                )
                .border(
                    width = 1.dp,
                    color = UiKitTheme.colors.border.general.active,
                    shape = CircleShape,
                ),
        ) {
            val text = if (clusterSize <= ClusterMaxSize) {
                clusterSize.toString()
            } else {
                "$ClusterMaxSize+"
            }
            Text(
                text = text,
                style = UiKitTheme.typography.tertiary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                maxLines = 1,
            )
        }
    }

    @Composable
    fun MyLocationButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        elevation: Dp = 4.dp,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.secondaryColors(),
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
            modifier = modifier.shadow(elevation),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_location_arrow_outline_24),
                contentDescription = stringResource(R.string.show_my_location),
                modifier = Modifier.size(20.dp),
            )
        }
    }

    private const val ClusterMaxSize = 99
}
