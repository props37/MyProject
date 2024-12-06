package ru.livetyping.zarina.feature.productlist.ui.impl.impl.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaFilterIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TopBarState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    state: TopBarState,
    onEvent: (TopBarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    ) {
        ZarinaBackIconButton(
            onClick = { onEvent(TopBarEvent.BackClicked) },
            iconSize = IconSize,
            modifier = Modifier.padding(start = 2.dp),
        )

        Spacer(modifier = Modifier.width(4.dp))

        AnimatedContent(
            targetState = state.categoryName,
            transitionSpec = { AnimatedContentCrossfadeTransitionSpec },
            contentAlignment = Alignment.CenterStart,
            label = "TopBar category name",
            modifier = Modifier.weight(1f),
        ) { categoryName ->
            val textStyle = UiKitTheme.typography.primary.regular
            if (categoryName != null) {
                Text(
                    text = categoryName,
                    style = textStyle,
                    color = UiKitTheme.colors.text.general.regular.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                ZarinaTextSkeleton(
                    textStyle = textStyle,
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.Start)
                        .fillMaxWidth(fraction = 0.5f),
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        ZarinaIconButton(
            onClick = { onEvent(TopBarEvent.SearchClicked) },
            indication = ripple(bounded = false, radius = IconSize),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_magnifying_glass_24),
                contentDescription = stringResource(RCommon.string.res_search_noun),
                tint = UiKitTheme.colors.icon.regular.default,
                modifier = Modifier.size(IconSize),
            )
        }

        ZarinaFilterIconButton(
            onClick = { onEvent(TopBarEvent.FiltersClicked) },
            appliedFilterCount = state.appliedFilterCount,
            iconSize = 20.dp,
            modifier = Modifier.padding(end = 2.dp),
        )
    }
}

private val IconSize: Dp get() = 20.dp
