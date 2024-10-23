package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBarDefaults

@Composable
internal fun TopBar(
    state: TopBarState,
    onEvent: (TopBarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            Text(
                text = stringResource(R.string.wishlist),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        endContent = {
            AnimatedVisibility(
                visible = state.isClearButtonVisible,
                enter = remember { AnimatedContentDefaultEnterTransition },
                exit = remember { AnimatedContentDefaultExitTransition },
            ) {
                ZarinaButton(
                    onClick = { onEvent(TopBarEvent.ClearClicked) },
                    size = ZarinaButtonSize.Small,
                    colors = ZarinaButtonDefaults.backlessColors(),
                    textStyle = UiKitTheme.typography.caption1.regular,
                    modifier = Modifier.padding(end = 8.dp),
                ) {
                    Text(text = stringResource(R.string.clear).uppercase())
                }
            }
        },
        contentPadding = ZarinaTopBarDefaults.ContentPaddingWithButtons,
        modifier = modifier,
    )
}
