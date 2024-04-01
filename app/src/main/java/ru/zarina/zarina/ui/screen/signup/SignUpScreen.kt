package ru.zarina.zarina.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.item.ZarinaItem
import ru.zarina.zarina.ui.common.component.switchh.ZarinaSwitch
import ru.zarina.zarina.ui.common.component.textfield.ZarinaPasswordTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.signup.SignUpScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.signup.SignUpViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun SignUpScreen(
    navigate: (SignUpScreenAction) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val receiveNewsNyEmail by viewModel.receiveNewsByEmail.collectAsStateWithLifecycle()
    val receiveSmsNotifications by viewModel.receiveSmsNotifications.collectAsStateWithLifecycle()

    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        name = name,
        onNameChanged = viewModel::onNameChanged,
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
        receiveNewsNyEmail = receiveNewsNyEmail,
        onReceiveNewsNyEmailChanged = viewModel::onReceiveNewsNyEmailChanged,
        receiveSmsNotifications = receiveSmsNotifications,
        onReceiveSmsNotificationsChanged = viewModel::onReceiveSmsNotificationsChanged,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    receiveNewsNyEmail: Boolean,
    onReceiveNewsNyEmailChanged: (Boolean) -> Unit,
    receiveSmsNotifications: Boolean,
    onReceiveSmsNotificationsChanged: (Boolean) -> Unit,
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
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            ),
    ) {
        TopBar(onBackClicked = onBackClicked)

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            ZarinaTextField(
                value = name,
                onValueChanged = onNameChanged,
                label = { Text(text = stringResource(R.string.first_name)) }, // TODO: [High] Update
                placeholder = { Text(text = stringResource(R.string.first_name)) }, // TODO: [High] Update
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = name.isNotEmpty(),
                        onClick = { onNameChanged("") },
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                value = email,
                onValueChanged = onEmailChanged,
                label = { Text(text = stringResource(R.string.email)) }, // TODO: [High] Update
                placeholder = { Text(text = stringResource(R.string.email)) }, // TODO: [High] Update
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = email.isNotEmpty(),
                        onClick = { onEmailChanged("") },
                    )
                },
                keyboardOptions = remember { KeyboardOptions(keyboardType = KeyboardType.Email) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                value = phone,
                onValueChanged = onPhoneChanged,
                label = { Text(text = stringResource(R.string.phone)) }, // TODO: [High] Update
                placeholder = { Text(text = stringResource(R.string.phone)) }, // TODO: [High] Update
                keyboardOptions = remember { KeyboardOptions(keyboardType = KeyboardType.Phone) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaPasswordTextField(
                password = password,
                onPasswordChanged = onPasswordChanged,
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
            val navigationBarsHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarsHeight))
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
