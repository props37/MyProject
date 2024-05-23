package ru.livetyping.zarina.presentation.screen.signup

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.datepicker.ZarinaDatePicker
import ru.livetyping.zarina.presentation.common.component.datepicker.ZarinaDatePickerDialog
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.switchh.ZarinaSwitch
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPasswordTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPhoneNumberTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.signup.SignUpScreenComponents.DatePickerMinYear
import ru.livetyping.zarina.presentation.screen.signup.SignUpScreenComponents.Policies
import ru.livetyping.zarina.presentation.screen.signup.SignUpScreenComponents.RecaptchaPolicies
import ru.livetyping.zarina.presentation.screen.signup.SignUpScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.signup.SignUpViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.navigationBarsOrIme
import ru.livetyping.zarina.util.compose.tryRequestFocus
import java.time.LocalDate

@Composable
fun SignUpScreen(
    navigate: (SignUpScreenAction) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val firstName by viewModel.firstName.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isFirstNameInvalid by viewModel.isFirstNameInvalid.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isPhoneInvalid by viewModel.isPhoneInvalid.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
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
        onBirthDateMillisChanged = viewModel::onBirthDateMillisChanged,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    isFirstNameInvalid: Boolean,
    onBirthDateMillisChanged: (Long?) -> Unit,
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
    val firstNameFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        firstNameFocusRequester.tryRequestFocus()
    }

    SignUpScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    var isDatePickerVisible by remember { mutableStateOf(false) }
    if (isDatePickerVisible) {
        val currentMillis = remember { System.currentTimeMillis() }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentMillis,
            yearRange = remember { DatePickerMinYear..LocalDate.now().year },
        )

        ZarinaDatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            confirmButton = {
                ZarinaButton(
                    onClick = {
                        onBirthDateMillisChanged(datePickerState.selectedDateMillis)
                        isDatePickerVisible = false
                    },
                    colors = ZarinaButtonDefaults.backlessColors(),
                ) {
                    Text(text = stringResource(R.string.select).uppercase())
                }
            },
        ) {
            ZarinaDatePicker(state = datePickerState)
        }
    }

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
                placeholder = {
                    Text(text = stringResource(R.string.first_name_text_field_placeholder))
                },
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
                    .padding(horizontal = 16.dp)
                    .focusRequester(firstNameFocusRequester),
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

            ZarinaDivider(
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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            onBackClicked = {},
            firstName = "",
            onFirstNameChanged = {},
            isFirstNameInvalid = false,
            onBirthDateMillisChanged = {},
            email = "",
            onEmailChanged = {},
            isEmailInvalid = false,
            phone = "",
            onPhoneChanged = {},
            isPhoneInvalid = false,
            password = "",
            onPasswordChanged = {},
            isPasswordInvalid = false,
            receiveNewsNyEmail = false,
            onReceiveNewsNyEmailChanged = {},
            receiveSmsNotifications = false,
            onReceiveSmsNotificationsChanged = {},
            arePoliciesAccepted = false,
            onPoliciesAcceptedChanged = {},
            isPoliciesErrorVisible = false,
            onUrlClicked = {},
            onSignUpClicked = {},
            isSignUpButtonLoading = false,
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
