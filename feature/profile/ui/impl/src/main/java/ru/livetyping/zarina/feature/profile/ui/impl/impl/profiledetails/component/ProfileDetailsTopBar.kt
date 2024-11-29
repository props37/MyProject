package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
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
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.ProfileDetailsTopBarState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ProfileDetailsTopBar(
    state: ProfileDetailsTopBarState,
    onEvent: (ProfileDetailsTopBarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = { onEvent(ProfileDetailsTopBarEvent.BackClicked) },
                iconSize = 20.dp,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            Text(
                text = stringResource(R.string.profile_account_details),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        endContent = {
            AnimatedVisibility(
                visible = state.isSaveButtonVisible,
                enter = AnimatedContentDefaultEnterTransition,
                exit = AnimatedContentDefaultExitTransition,
            ) {
                ZarinaButton(
                    onClick = { onEvent(ProfileDetailsTopBarEvent.SaveClicked) },
                    size = ZarinaButtonSize.Medium,
                    colors = ZarinaButtonDefaults.backlessColors(),
                    modifier = Modifier
                        .heightIn(min = 40.dp)
                        .padding(end = 8.dp),
                ) {
                    Text(text = stringResource(RCommon.string.res_save).uppercase())
                }
            }
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
