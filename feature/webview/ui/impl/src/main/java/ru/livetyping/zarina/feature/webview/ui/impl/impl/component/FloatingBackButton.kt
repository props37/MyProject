package ru.livetyping.zarina.feature.webview.ui.impl.impl.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun FloatingBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaButton(
        onClick = onClick,
        size = ZarinaButtonSize.Small,
        colors = ZarinaButtonDefaults.secondaryColors(),
        contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
            contentDescription = stringResource(RCommon.string.res_back),
            modifier = Modifier
                .size(20.dp)
                .rotate(Rotation),
        )
    }
}

private const val Rotation = 270f
