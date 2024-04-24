package ru.livetyping.zarina.ui.screen.profile.details.accountdeletionconfirmation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.profile.details.accountdeletionconfirmation.AccountDeletionConfirmationViewModel.SideEffect

@Composable
fun AccountDeletionConfirmationDialogScreen(
    navigate: (AccountDeletionConfirmationScreenAction) -> Unit,
    viewModel: AccountDeletionConfirmationViewModel = hiltViewModel(),
) {
    val isDeleteButtonLoading by viewModel.isDeleteButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        isDeleteButtonLoading = isDeleteButtonLoading,
        onKeepClicked = viewModel::onKeepClicked,
        onDeleteClicked = viewModel::onDeleteClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    isDeleteButtonLoading: Boolean,
    onKeepClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (AccountDeletionConfirmationScreenAction) -> Unit,
) {
    AccountDeletionConfirmationScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    ZarinaDialogContainer(
        title = {
            Text(text = stringResource(R.string.delete_account_question))
        },
        body = {
            Text(text = stringResource(R.string.account_deletion_confirmation_body))
        },
        buttons = {
            ZarinaButton(
                onClick = onKeepClicked,
                colors = ZarinaButtonDefaults.tertiaryColors(),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.keep).uppercase())
            }

            Spacer(modifier = Modifier.width(8.dp))

            ZarinaButton(
                onClick = onDeleteClicked,
                isLoading = isDeleteButtonLoading,
                colors = ZarinaButtonDefaults.outlineErrorColors(),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.delete).uppercase())
            }
        },
    )
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            isDeleteButtonLoading = false,
            onKeepClicked = {},
            onDeleteClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {}
        )
    }
}
