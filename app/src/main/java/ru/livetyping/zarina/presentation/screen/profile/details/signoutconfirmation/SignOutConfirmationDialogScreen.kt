package ru.livetyping.zarina.presentation.screen.profile.details.signoutconfirmation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.dialog.ZarinaDialogContainer
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview

// TODO: [Low] Make internal and remove from navigation
@Composable
fun SignOutConfirmationDialogScreen(
    navigate: (SignOutConfirmationScreenAction) -> Unit,
    viewModel: SignOutConfirmationViewModel = hiltViewModel(),
) {
    val isSignOutButtonLoading by viewModel.isSignOutButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        isSignOutButtonLoading = isSignOutButtonLoading,
        onStayClicked = viewModel::onStayClicked,
        onSignOutClicked = viewModel::onSignOutClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    isSignOutButtonLoading: Boolean,
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
            Text(text = stringResource(R.string.sign_out_confirmation_body))
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
                isLoading = isSignOutButtonLoading,
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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            isSignOutButtonLoading = false,
            onStayClicked = {},
            onSignOutClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
