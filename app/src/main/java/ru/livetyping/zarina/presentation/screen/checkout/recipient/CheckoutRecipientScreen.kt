package ru.livetyping.zarina.presentation.screen.checkout.recipient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPhoneNumberTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents
import ru.livetyping.zarina.presentation.screen.checkout.recipient.CheckoutRecipientViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.autofill.autofill

@Composable
fun CheckoutRecipientScreen(
    navigate: (CheckoutRecipientScreenAction) -> Unit,
    viewModel: CheckoutRecipientViewModel = hiltViewModel(),
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val stepCount by viewModel.stepCount.collectAsStateWithLifecycle()
    val isFirstNameInvalid by viewModel.isFirstNameInvalid.collectAsStateWithLifecycle()
    val isLastNameInvalid by viewModel.isLastNameInvalid.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val isPhoneInvalid by viewModel.isPhoneInvalid.collectAsStateWithLifecycle()
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()

    ScreenContent(
        step = step,
        stepCount = stepCount,
        firstNameTextFieldState = viewModel.firstNameTextFieldState,
        isFirstNameInvalid = isFirstNameInvalid,
        lastNameTextFieldState = viewModel.lastNameTextFieldState,
        isLastNameInvalid = isLastNameInvalid,
        phone = phone,
        isPhoneInvalid = isPhoneInvalid,
        onPhoneChanged = viewModel::onPhoneChanged,
        emailTextFieldState = viewModel.emailTextFieldState,
        isEmailInvalid = isEmailInvalid,
        onContinueClicked = viewModel::onContinueClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScreenContent(
    step: Int,
    stepCount: Int,
    firstNameTextFieldState: TextFieldState,
    isFirstNameInvalid: Boolean,
    lastNameTextFieldState: TextFieldState,
    isLastNameInvalid: Boolean,
    phone: String,
    isPhoneInvalid: Boolean,
    onPhoneChanged: (String) -> Unit,
    emailTextFieldState: TextFieldState,
    isEmailInvalid: Boolean,
    onContinueClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutRecipientScreenAction) -> Unit,
) {
    CheckoutRecipientScreenBehavior(
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
            )
            .imePadding(),
    ) {
        CheckoutComponents.TopBar(
            title = stringResource(R.string.recipient),
            step = step,
            stepCount = stepCount,
            isBackButtonVisible = false,
            onCloseClicked = onCloseClicked,
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.personal_data),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = lastNameTextFieldState,
                isError = isLastNameInvalid,
                label = {
                    Text(text = stringResource(R.string.last_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.last_name_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = lastNameTextFieldState.text.isNotEmpty(),
                        onClick = { lastNameTextFieldState.clearText() },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    )
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .autofill(
                        autofillType = AutofillType.PersonLastName,
                        onFilled = { lastNameTextFieldState.setTextAndPlaceCursorAtEnd(it) },
                    ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = firstNameTextFieldState,
                isError = isFirstNameInvalid,
                label = {
                    Text(text = stringResource(R.string.first_name))
                },
                placeholder = {
                    Text(text = stringResource(R.string.first_name_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = firstNameTextFieldState.text.isNotEmpty(),
                        onClick = { firstNameTextFieldState.clearText() },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    )
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .autofill(
                        autofillType = AutofillType.PersonFirstName,
                        onFilled = { firstNameTextFieldState.setTextAndPlaceCursorAtEnd(it) },
                    ),
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.contacts),
                style = UiKitTheme.typography.secondary.bold,
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
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .autofill(
                        autofillType = AutofillType.PhoneNumber,
                        onFilled = { onPhoneChanged(it) },
                    ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaTextField(
                state = emailTextFieldState,
                isError = isEmailInvalid,
                label = {
                    Text(text = stringResource(R.string.email))
                },
                placeholder = {
                    Text(text = stringResource(R.string.email_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = emailTextFieldState.text.isNotEmpty(),
                        onClick = { emailTextFieldState.clearText() },
                    )
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    )
                },
                onKeyboardAction = { onContinueClicked() },
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.weight(1f))
            ZarinaButton(
                onClick = onContinueClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.continue_).uppercase())
            }
            val navigationBarHeight =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarHeight))
            Spacer(modifier = Modifier.height(20.dp))
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
            step = 1,
            stepCount = 4,
            firstNameTextFieldState = rememberTextFieldState(),
            isFirstNameInvalid = false,
            lastNameTextFieldState = rememberTextFieldState(),
            isLastNameInvalid = false,
            phone = "",
            isPhoneInvalid = false,
            onPhoneChanged = {},
            emailTextFieldState = rememberTextFieldState(),
            isEmailInvalid = false,
            onContinueClicked = {},
            onCloseClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
