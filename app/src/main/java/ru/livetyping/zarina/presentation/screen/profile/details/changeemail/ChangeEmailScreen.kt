package ru.livetyping.zarina.presentation.screen.profile.details.changeemail

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.profile.details.changeemail.ChangeEmailScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.profile.details.changeemail.ChangeEmailViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.tryRequestFocus

@Composable
fun ChangeEmailScreen(
    navigate: (ChangeEmailScreenAction) -> Unit,
    viewModel: ChangeEmailViewModel = hiltViewModel(),
) {
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()
    val isChangeEmailButtonLoading by viewModel.isChangeEmailButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        onEmailChanged = viewModel::onEmailChanged,
        isEmailInvalid = isEmailInvalid,
        onEmailEntered = viewModel::onEmailEntered,
        onChangeEmailClicked = viewModel::onChangeEmailClicked,
        isChangeEmailButtonLoading = isChangeEmailButtonLoading,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onEmailChanged: (String) -> Unit,
    isEmailInvalid: Boolean,
    onEmailEntered: () -> Unit,
    onChangeEmailClicked: () -> Unit,
    isChangeEmailButtonLoading: Boolean,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ChangeEmailScreenAction) -> Unit,
) {
    ChangeEmailScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.tryRequestFocus()
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

            Text(
                text = stringResource(R.string.enter_new_email),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = rememberTextFieldState(),
                onTextChanged = onEmailChanged,
                isError = isEmailInvalid,
                label = {
                    Text(text = stringResource(R.string.email))
                },
                placeholder = {
                    Text(text = stringResource(R.string.email_text_field_placeholder))
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    )
                },
                onKeyboardAction = { onEmailEntered() },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
            )
            Spacer(modifier = Modifier.height(32.dp))

            ZarinaButton(
                onClick = onChangeEmailClicked,
                isLoading = isChangeEmailButtonLoading,
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
        // TODO: [Low] Add preview
    }
}
