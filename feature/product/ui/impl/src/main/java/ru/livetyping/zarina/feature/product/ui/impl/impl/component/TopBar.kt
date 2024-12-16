package ru.livetyping.zarina.feature.product.ui.impl.impl.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarMode
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    state: TopBarState,
    onEvent: (TopBarEvent) -> Unit,
    mode: TopBarMode,
    windowInsets: WindowInsets,
    modifier: Modifier = Modifier,
) {
    val backgroundAlpha = animateFloatAsState(
        targetValue = when (mode) {
            TopBarMode.Transparent -> 0f
            TopBarMode.Filled -> 1f
        },
        label = "top bar alpha",
    )
    val backgroundColor = UiKitTheme.colors.background.general.regular.default

    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = { onEvent(TopBarEvent.BackClicked) },
                iconSize = IconSize,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            Text(
                text = state.productName.orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.graphicsLayer { alpha = backgroundAlpha.value },
            )
        },
        endContent = {
            ZarinaIconButton(
                onClick = { onEvent(TopBarEvent.ShareClicked) },
                indication = ripple(bounded = false, radius = IconSize),
                modifier = Modifier.padding(end = 2.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(RCommon.drawable.ic_share_24),
                    contentDescription = stringResource(RCommon.string.res_share),
                    modifier = Modifier.size(IconSize),
                )
            }
        },
        backgroundColor = Color.Unspecified,
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier
            .drawBehind {
                drawRect(color = backgroundColor, alpha = backgroundAlpha.value)
            }
            .windowInsetsPadding(windowInsets),
    )
}

@Composable
internal fun topBarModeAsState(lazyListState: LazyListState): State<TopBarMode> {
    return remember {
        derivedStateOf {
            val visibleItemsInfo = lazyListState.layoutInfo.visibleItemsInfo
            val firstVisibleItemKey = visibleItemsInfo.firstOrNull()?.key
            if (firstVisibleItemKey == ProductListKey.MediaPager) {
                TopBarMode.Transparent
            } else {
                TopBarMode.Filled
            }
        }
    }
}

private val IconSize: Dp get() = 20.dp
