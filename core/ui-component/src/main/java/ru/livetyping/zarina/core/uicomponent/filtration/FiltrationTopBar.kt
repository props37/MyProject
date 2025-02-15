package ru.livetyping.zarina.core.uicomponent.filtration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun FiltrationTopBar(
    state: FiltrationTopBarState,
    onEvent: (FiltrationTopBarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = { onEvent(FiltrationTopBarEvent.BackClicked) },
                iconSize = 20.dp,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            Text(
                text = stringResource(RCommon.string.res_filters),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        endContent = {
            AnimatedVisibility(
                visible = state.isResetFiltersButtonVisible,
                enter = AnimatedContentDefaultEnterTransition,
                exit = AnimatedContentDefaultExitTransition,
            ) {
                ZarinaButton(
                    onClick = { onEvent(FiltrationTopBarEvent.ResetFiltersClicked) },
                    size = ZarinaButtonSize.Small,
                    colors = ZarinaButtonDefaults.backlessColors(),
                    modifier = Modifier.padding(end = 8.dp),
                ) {
                    Text(
                        text = stringResource(RCommon.string.res_reset).uppercase(),
                        style = UiKitTheme.typography.caption1.regular,
                    )
                }
            }
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
