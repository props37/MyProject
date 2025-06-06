package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    backgroundAlphaProvider: () -> Float,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = onBackClicked,
                iconSize = IconSize,
            )
        },
        endContent = {
            ZarinaIconButton(
                onClick = onShareClicked,
                indication = ripple(radius = IconSize, bounded = false),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(RCommon.drawable.ic_leave_24),
                    contentDescription = stringResource(RCommon.string.res_share),
                    modifier = Modifier
                        .size(IconSize)
                        .rotate(90f),
                )
            }
        },
        backgroundColor = UiKitTheme2.colors.white.copy(alpha = backgroundAlphaProvider()),
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}

private val IconSize: Dp get() = 16.dp
