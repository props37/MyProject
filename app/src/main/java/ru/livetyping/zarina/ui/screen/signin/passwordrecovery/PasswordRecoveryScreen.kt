package ru.livetyping.zarina.ui.screen.signin.passwordrecovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.signin.passwordrecovery.PasswordRecoveryScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.signin.passwordrecovery.PasswordRecoveryViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.navigationBarsOrIme

@Composable
fun PasswordRecoveryScreen(
    navigate: (PasswordRecoveryScreenAction) -> Unit,
    viewModel: PasswordRecoveryViewModel = hiltViewModel(),
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()
    val isSendButtonLoading by viewModel.isSendButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        isEmailInvalid = isEmailInvalid,
        onSendClicked = viewModel::onSendClicked,
        isSendButtonLoading = isSendButtonLoading,
        onBackClicked = {},
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    email: String,
    onEmailChanged: (String) -> Unit,
    isEmailInvalid: Boolean,
    onSendClicked: () -> Unit,
    isSendButtonLoading: Boolean,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (PasswordRecoveryScreenAction) -> Unit,
) {
    PasswordRecoveryScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(onBackClicked = onBackClicked)

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.enter_email_specified_during_registration),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.we_will_send_link_to_reset_password_to_specified_email),
                style = UiKitTheme.typography.tertiary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                value = email,
                onValueChanged = onEmailChanged,
                isError = isEmailInvalid,
                label = { Text(text = stringResource(R.string.email)) },
                placeholder = { Text(text = stringResource(R.string.email_text_field_placeholder)) },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = email.isNotEmpty(),
                        onClick = { onEmailChanged("") },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(48.dp))
            
            ZarinaButton(
                onClick = onSendClicked,
                isLoading = isSendButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.send).uppercase())
            }

            Spacer(modifier = Modifier.height(20.dp))
            val navigationBarsOrImeBottomPadding =
                WindowInsets.navigationBarsOrIme.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarsOrImeBottomPadding))
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            email = "",
            onEmailChanged = {},
            isEmailInvalid = false,
            onSendClicked = {},
            isSendButtonLoading = false,
            onBackClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
