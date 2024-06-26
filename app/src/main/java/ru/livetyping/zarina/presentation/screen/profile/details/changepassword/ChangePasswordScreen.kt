package ru.livetyping.zarina.presentation.screen.profile.details.changepassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPasswordTextField
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.profile.details.changepassword.ChangePasswordScreenComponents.TopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.tryRequestFocus

@Composable
fun ChangePasswordScreen(
    navigate: (ChangePasswordScreenAction) -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val oldPassword by viewModel.oldPassword.collectAsStateWithLifecycle()
    val isOldPasswordInvalid by viewModel.isOldPasswordInvalid.collectAsStateWithLifecycle()
    val newPassword by viewModel.newPassword.collectAsStateWithLifecycle()
    val isNewPasswordInvalid by viewModel.isNewPasswordInvalid.collectAsStateWithLifecycle()
    val isChangePasswordButtonLoading by viewModel.isChangePasswordButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        oldPassword = oldPassword,
        isOldPasswordInvalid = isOldPasswordInvalid,
        onOldPasswordChanged = viewModel::onOldPasswordChanged,
        newPassword = newPassword,
        isNewPasswordInvalid = isNewPasswordInvalid,
        onNewPasswordChanged = viewModel::onNewPasswordChanged,
        onNewPasswordEntered = viewModel::onNewPasswordEntered,
        onChangePasswordClicked = viewModel::onChangePasswordClicked,
        isChangePasswordButtonLoading = isChangePasswordButtonLoading,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    oldPassword: String,
    isOldPasswordInvalid: Boolean,
    onOldPasswordChanged: (String) -> Unit,
    newPassword: String,
    isNewPasswordInvalid: Boolean,
    onNewPasswordChanged: (String) -> Unit,
    onNewPasswordEntered: () -> Unit,
    onChangePasswordClicked: () -> Unit,
    isChangePasswordButtonLoading: Boolean,
    onBackClicked: () -> Unit,
    sideEffects: Flow<ChangePasswordViewModel.SideEffect>,
    navigate: (ChangePasswordScreenAction) -> Unit,
) {
    ChangePasswordScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val oldPasswordFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        oldPasswordFocusRequester.tryRequestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(onBackClicked = onBackClicked)

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            ZarinaPasswordTextField(
                password = oldPassword,
                onPasswordChanged = onOldPasswordChanged,
                isError = isOldPasswordInvalid,
                label = stringResource(R.string.old_password),
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(oldPasswordFocusRequester),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaPasswordTextField(
                password = newPassword,
                onPasswordChanged = onNewPasswordChanged,
                isError = isNewPasswordInvalid,
                label = stringResource(R.string.new_password),
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    )
                },
                keyboardActions = remember(onNewPasswordEntered) {
                    KeyboardActions(
                        onDone = { onNewPasswordEntered() },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))

            ZarinaButton(
                onClick = onChangePasswordClicked,
                isLoading = isChangePasswordButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.change).uppercase())
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            oldPassword = "",
            isOldPasswordInvalid = false,
            onOldPasswordChanged = {},
            newPassword = "",
            isNewPasswordInvalid = false,
            onNewPasswordChanged = {},
            onNewPasswordEntered = {},
            onChangePasswordClicked = {},
            isChangePasswordButtonLoading = false,
            onBackClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
