package ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview

@Composable
fun SignOutConfirmationDialogScreen(
    navigate: (SignOutConfirmationScreenAction) -> Unit,
    viewModel: SignOutConfirmationViewModel = hiltViewModel(),
) {
    DialogContent(
        onStayClicked = viewModel::onStayClicked,
        onSignOutClicked = viewModel::onSignOutClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun DialogContent(
    onStayClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    sideEffects: Flow<SignOutConfirmationViewModel.SideEffect>,
    navigate: (SignOutConfirmationScreenAction) -> Unit,
) {
    SignOutConfirmationScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    ZarinaDialogContainer(
        title = {
            Text(text = stringResource(R.string.sign_out_question))
        },
        body = {
            Text(text = stringResource(R.string.sign_out_confirmation_description))
        },
        buttons = {
            ZarinaButton(
                onClick = onStayClicked,
                colors = ZarinaButtonDefaults.outlineColors(),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.stay).uppercase(),
                    maxLines = 1,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            ZarinaButton(
                onClick = onSignOutClicked,
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.go_out).uppercase(),
                    maxLines = 1,
                )
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
        DialogContent(
            onStayClicked = {},
            onSignOutClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
