package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ProfileTopBar(
    userFirstName: String?,
    isProfileDetailsButtonVisible: Boolean,
    onProfileDetailsClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(RCommon.string.res_profile),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                AnimatedContent(
                    targetState = userFirstName,
                    transitionSpec = {
                        AnimatedContentDefaultTransitionSpec
                            .using(SizeTransform(clip = false))
                    },
                    contentAlignment = Alignment.Center,
                    label = "User first name",
                ) { firstName ->
                    if (firstName != null) {
                        Text(
                            text = firstName,
                            style = UiKitTheme.typography.tertiary.regular,
                            color = UiKitTheme.colors.text.general.regular.muted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        },
        endContent = {
            AnimatedVisibility(
                visible = isProfileDetailsButtonVisible,
                enter = remember { AnimatedContentDefaultEnterTransition },
                exit = remember { AnimatedContentDefaultExitTransition },
            ) {
                ZarinaIconButton(
                    onClick = onProfileDetailsClicked,
                    indication = ripple(bounded = false, radius = 20.dp),
                    modifier = Modifier.padding(end = 2.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_pencil_24),
                        contentDescription = stringResource(R.string.profile_edit_profile),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}
