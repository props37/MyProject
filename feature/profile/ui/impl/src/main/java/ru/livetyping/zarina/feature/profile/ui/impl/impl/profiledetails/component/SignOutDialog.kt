package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.SignOutDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.SignOutDialogState

@Composable
internal fun SignOutDialog(
    state: SignOutDialogState.Visible,
    onEvent: (SignOutDialogEvent) -> Unit,
) {
    Dialog(
        onDismissRequest = { onEvent(SignOutDialogEvent.DismissRequested) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        ZarinaDialogContainer(
            title = {
                Text(text = stringResource(R.string.profile_sign_out_question))
            },
            body = {
                Text(text = stringResource(R.string.profile_sign_out_confirmation_body))
            },
            buttons = {
                ZarinaButton(
                    onClick = { onEvent(SignOutDialogEvent.DismissRequested) },
                    colors = ZarinaButtonDefaults.outlineColors(),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.profile_stay).uppercase(),
                        maxLines = 1,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                ZarinaButton(
                    onClick = { onEvent(SignOutDialogEvent.SignOutClicked) },
                    isLoading = state.isSignOutButtonLoading,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.profile_sign_out_short).uppercase(),
                        maxLines = 1,
                    )
                }
            },
        )
    }
}
