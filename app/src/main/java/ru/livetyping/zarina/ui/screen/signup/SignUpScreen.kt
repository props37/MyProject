package ru.livetyping.zarina.ui.screen.signup

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
import androidx.compose.material.Divider
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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.item.ZarinaItem
import ru.livetyping.zarina.ui.common.component.switchh.ZarinaSwitch
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaPasswordTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaPhoneNumberTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.signup.SignUpScreenComponents.Policies
import ru.livetyping.zarina.ui.screen.signup.SignUpScreenComponents.RecaptchaPolicies
import ru.livetyping.zarina.ui.screen.signup.SignUpScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.signup.SignUpViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.navigationBarsOrIme

@Composable
fun SignUpScreen(
    navigate: (SignUpScreenAction) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val firstName by viewModel.firstName.collectAsStateWithLifecycle()
    val isFirstNameInvalid by viewModel.isFirstNameInvalid.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val isPhoneInvalid by viewModel.isPhoneInvalid.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isPasswordInvalid by viewModel.isPasswordInvalid.collectAsStateWithLifecycle()
    val receiveNewsNyEmail by viewModel.receiveNewsByEmail.collectAsStateWithLifecycle()
    val receiveSmsNotifications by viewModel.receiveSmsNotifications.collectAsStateWithLifecycle()
    val arePoliciesAccepted by viewModel.arePoliciesAccepted.collectAsStateWithLifecycle()
    val isPoliciesErrorVisible by viewModel.isPoliciesErrorVisible.collectAsStateWithLifecycle()
    val isSignUpButtonLoading by viewModel.isSignUpButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        firstName = firstName,
        onFirstNameChanged = viewModel::onFirstNameChanged,
        isFirstNameInvalid = isFirstNameInvalid,
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        isEmailInvalid = isEmailInvalid,
        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        isPhoneInvalid = isPhoneInvalid,
        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
        isPasswordInvalid = isPasswordInvalid,
        receiveNewsNyEmail = receiveNewsNyEmail,
        onReceiveNewsNyEmailChanged = viewModel::onReceiveNewsNyEmailChanged,
        receiveSmsNotifications = receiveSmsNotifications,
        onReceiveSmsNotificationsChanged = viewModel::onReceiveSmsNotificationsChanged,
        arePoliciesAccepted = arePoliciesAccepted,
        onPoliciesAcceptedChanged = viewModel::onPoliciesAcceptedChanged,
        isPoliciesErrorVisible = isPoliciesErrorVisible,
        onUrlClicked = viewModel::onUrlClicked,
        onSignUpClicked = viewModel::onSignUpClicked,
        isSignUpButtonLoading = isSignUpButtonLoading,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    isFirstNameInvalid: Boolean,
    email: String,
    onEmailChanged: (String) -> Unit,
    isEmailInvalid: Boolean,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    isPhoneInvalid: Boolean,
    password: String,
    onPasswordChanged: (String) -> Unit,
    isPasswordInvalid: Boolean,
    receiveNewsNyEmail: Boolean,
    onReceiveNewsNyEmailChanged: (Boolean) -> Unit,
    receiveSmsNotifications: Boolean,
    onReceiveSmsNotificationsChanged: (Boolean) -> Unit,
    arePoliciesAccepted: Boolean,
    onPoliciesAcceptedChanged: (Boolean) -> Unit,
    isPoliciesErrorVisible: Boolean,
    onUrlClicked: (Url) -> Unit,
    onSignUpClicked: () -> Unit,
    isSignUpButtonLoading: Boolean,
    sideEffects: Flow<SideEffect>,
    navigate: (SignUpScreenAction) -> Unit,
) {
    SignUpScreenBehavior(
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

            ZarinaTextField(
                value = firstName,
                onValueChanged = onFirstNameChanged,
                isError = isFirstNameInvalid,
                label = { Text(text = stringResource(R.string.first_name)) },
                placeholder = { Text(text = stringResource(R.string.first_name)) },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = firstName.isNotEmpty(),
                        onClick = { onFirstNameChanged("") },
                    )
                },
                keyboardOptions = remember { KeyboardOptions(imeAction = ImeAction.Next) },
                singleLine = true,
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
                placeholder = { Text(text = stringResource(R.string.email)) },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = email.isNotEmpty(),
                        onClick = { onEmailChanged("") },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaPhoneNumberTextField(
                phoneNumber = phone,
                onPhoneNumberChanged = onPhoneChanged,
                isError = isPhoneInvalid,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaPasswordTextField(
                password = password,
                onPasswordChanged = onPasswordChanged,
                isError = isPasswordInvalid,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            ZarinaItem(
                modifier = Modifier.fillMaxWidth(),
                startContent = {
                    Text(
                        text = stringResource(R.string.receive_news_by_email),
                        style = UiKitTheme.typography.secondary.light,
                    )
                },
                endContent = {
                    ZarinaSwitch(
                        isChecked = receiveNewsNyEmail,
                        onCheckedChanged = onReceiveNewsNyEmailChanged,
                    )
                },
            )

            Divider(
                color = UiKitTheme.colors.border.general.default,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            ZarinaItem(
                modifier = Modifier.fillMaxWidth(),
                startContent = {
                    Text(
                        text = stringResource(R.string.receive_sms_notifications),
                        style = UiKitTheme.typography.secondary.light,
                        modifier = Modifier.weight(1f),
                    )
                },
                endContent = {
                    ZarinaSwitch(
                        isChecked = receiveSmsNotifications,
                        onCheckedChanged = onReceiveSmsNotificationsChanged,
                    )
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Policies(
                areAccepted = arePoliciesAccepted,
                onAcceptedChanged = onPoliciesAcceptedChanged,
                isError = isPoliciesErrorVisible,
                onUrlClicked = onUrlClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(36.dp))

            ZarinaButton(
                onClick = onSignUpClicked,
                isLoading = isSignUpButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.continue_).uppercase())
            }

            Spacer(modifier = Modifier.height(16.dp))

            RecaptchaPolicies(
                onUrlClicked = onUrlClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

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
        // TODO: [Low] Add preview
    }
}
