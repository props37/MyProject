package ru.livetyping.zarina.core.uikit.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ZarinaScrollToTopButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = remember { AnimatedContentDefaultEnterTransition },
        exit = remember { AnimatedContentDefaultExitTransition },
        modifier = modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Medium,
            shape = CircleShape,
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_arrow_left_24),
                contentDescription = stringResource(RCommon.string.res_scroll_to_top),
                modifier = Modifier
                    .size(16.dp)
                    .rotate(degrees = 90f),
            )
        }
    }
}
