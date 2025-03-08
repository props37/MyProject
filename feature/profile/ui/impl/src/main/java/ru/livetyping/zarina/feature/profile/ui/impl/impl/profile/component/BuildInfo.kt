package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component

import androidx.compose.foundation.combinedClickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.platform.copyTextToClipboard
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileState

@Composable
internal fun BuildInfo(
    buildInfo: ProfileState.BuildInfo,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val appVersionTitle = stringResource(R.string.profile_app_version)
    val text = "$appVersionTitle ${buildInfo.appVersion}"

    Text(
        text = text,
        style = UiKitTheme.typography.footnote.regular,
        color = UiKitTheme.colors.text.general.regular.muted,
        modifier = modifier.combinedClickable(
            interactionSource = null,
            indication = null,
            hapticFeedbackEnabled = buildInfo.mindboxDeviceUuid != null,
            onClick = {
                context.copyTextToClipboard(appVersionTitle, buildInfo.appVersion)
            },
            onLongClick = {
                if (buildInfo.mindboxDeviceUuid != null) {
                    val label = context.getString(R.string.profile_mindbox_device_uuid)
                    context.copyTextToClipboard(label, buildInfo.mindboxDeviceUuid)
                }
            },
        ),
    )
}
