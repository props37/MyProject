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
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.AccountDeletionDialogEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model.AccountDeletionDialogState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun AccountDeletionDialog(
    state: AccountDeletionDialogState.Visible,
    onEvent: (AccountDeletionDialogEvent) -> Unit,
) {
    Dialog(
        onDismissRequest = { onEvent(AccountDeletionDialogEvent.DismissRequested) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        ZarinaDialogContainer(
            title = {
                Text(text = stringResource(R.string.profile_delete_account_question))
            },
            body = {
                Text(text = stringResource(R.string.profile_account_deletion_confirmation_body))
            },
            buttons = {
                ZarinaButton(
                    onClick = { onEvent(AccountDeletionDialogEvent.DismissRequested) },
                    colors = ZarinaButtonDefaults.outlineColors(),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.profile_keep).uppercase(),
                        maxLines = 1,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                ZarinaButton(
                    onClick = { onEvent(AccountDeletionDialogEvent.DeleteAccountClicked) },
                    isLoading = state.isDeleteButtonLoading,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(RCommon.string.res_delete).uppercase(),
                        maxLines = 1,
                    )
                }
            },
        )
    }
}
