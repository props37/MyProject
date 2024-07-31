package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber

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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.policy.RecaptchaPolicy
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPhoneNumberTextField
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.ChangePhoneNumberScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.ChangePhoneNumberViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.tryRequestFocus

@Composable
fun ChangePhoneNumberScreen(
    navigate: (ChangePhoneNumberScreenAction) -> Unit,
    viewModel: ChangePhoneNumberViewModel = hiltViewModel(),
) {
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val isPhoneInvalid by viewModel.isPhoneInvalid.collectAsStateWithLifecycle()
    val isChangePhoneButtonLoading by viewModel.isChangePhoneButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        onPhoneEntered = viewModel::onPhoneEntered,
        isPhoneInvalid = isPhoneInvalid,
        onChangePhoneClicked = viewModel::onChangePhoneClicked,
        isChangePhoneButtonLoading = isChangePhoneButtonLoading,
        onUrlClicked = viewModel::onUrlClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    phone: String,
    onPhoneChanged: (String) -> Unit,
    onPhoneEntered: () -> Unit,
    isPhoneInvalid: Boolean,
    onChangePhoneClicked: () -> Unit,
    isChangePhoneButtonLoading: Boolean,
    onUrlClicked: (Url) -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ChangePhoneNumberScreenAction) -> Unit,
) {
    ChangePhoneNumberScreenBehavior(
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
                text = stringResource(R.string.we_will_send_code_for_changing_phone_number),
                style = UiKitTheme.typography.tertiary.light,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaPhoneNumberTextField(
                phoneNumber = phone,
                onPhoneNumberChanged = onPhoneChanged,
                isError = isPhoneInvalid,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                    )
                },
                keyboardActions = remember(onPhoneEntered) {
                    KeyboardActions(
                        onDone = { onPhoneEntered() },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
            )
            Spacer(modifier = Modifier.height(32.dp))

            ZarinaButton(
                onClick = onChangePhoneClicked,
                isLoading = isChangePhoneButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.continue_).uppercase())
            }
            Spacer(modifier = Modifier.height(16.dp))

            RecaptchaPolicy(
                onUrlClicked = onUrlClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

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
