package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.kotlinutil.LocalDateUtil
import ru.livetyping.zarina.core.uicommon.DateTimeUtils
import ru.livetyping.zarina.core.uicompose.rememberFormattedLocalDate
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaPolicies
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.switchh.ZarinaSwitch
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaPhoneTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.signup.ui.impl.R
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpEvent
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpState
import java.time.ZoneOffset
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SignUpScreenContent(
    signUpState: SignUpState,
    onSignUpEvent: (SignUpEvent) -> Unit,
    onBirthDateClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(24.dp))

        PersonalDataFields(
            nameTextFieldState = signUpState.nameTextFieldState,
            isNameInvalid = signUpState.isNameInvalid,
            birthDateEpochMillis = signUpState.birthDateEpochMillis,
            isBirthDateInvalid = signUpState.isBirthDateInvalid,
            emailTextFieldState = signUpState.emailTextFieldState,
            isEmailInvalid = signUpState.isEmailInvalid,
            phoneTextFieldState = signUpState.phoneTextFieldState,
            isPhoneInvalid = signUpState.isPhoneInvalid,
            passwordTextFieldState = signUpState.passwordTextFieldState,
            isPasswordInvalid = signUpState.isPasswordInvalid,
            onBirthDateClicked = onBirthDateClicked,
        )
        Spacer(modifier = Modifier.height(24.dp))

        SubscriptionSetup(
            receiveEmails = signUpState.receiveEmails,
            onReceiveEmailsChanged = {
                onSignUpEvent(SignUpEvent.ReceiveEmailsChanged(it))
            },
            receiveSms = signUpState.receiveSms,
            onReceiveSmsChanged = {
                onSignUpEvent(SignUpEvent.ReceiveSmsChanged(it))
            },
        )
        Spacer(modifier = Modifier.height(16.dp))

        Policies(
            areAccepted = signUpState.arePoliciesAccepted,
            onAcceptedChanged = { onSignUpEvent(SignUpEvent.PoliciesAcceptedChanged(it)) },
            isError = signUpState.arePoliciesInvalid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        SubscriptionPolicy(
            isVisible = signUpState.isSubscriptionPolicyVisible,
            isAccepted = signUpState.isSubscriptionPolicyAccepted,
            onAcceptedChanged = { onSignUpEvent(SignUpEvent.SubscriptionPolicyAcceptedChanged(it)) },
            isError = signUpState.isSubscriptionPolicyInvalid,
            contentPadding = PaddingValues(top = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(36.dp))

        ZarinaButton(
            onClick = { onSignUpEvent(SignUpEvent.SignUpClicked) },
            isLoading = signUpState.isSignUpButtonLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(text = stringResource(RCommon.string.res_continue).uppercase())
        }
        Spacer(modifier = Modifier.height(16.dp))

        YandexCaptchaPolicies(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
    }
}

@Suppress("UnusedReceiverParameter")
@Composable
private fun ColumnScope.PersonalDataFields(
    nameTextFieldState: TextFieldState,
    isNameInvalid: Boolean,
    birthDateEpochMillis: Long?,
    isBirthDateInvalid: Boolean,
    emailTextFieldState: TextFieldState,
    isEmailInvalid: Boolean,
    phoneTextFieldState: TextFieldState,
    isPhoneInvalid: Boolean,
    passwordTextFieldState: TextFieldState,
    isPasswordInvalid: Boolean,
    onBirthDateClicked: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        withFrameMillis {}
        nameFocusRequester.tryRequestFocus()
    }

    ZarinaTextField(
        state = nameTextFieldState,
        isError = isNameInvalid,
        label = {
            val label = if (nameTextFieldState.text.isNotEmpty()) {
                stringResource(RCommon.string.res_first_name)
            } else ""

            Text(text = label.uppercase())
        },
        placeholder = {
            Text(text = stringResource(RCommon.string.res_first_name).uppercase())
        },
        innerTrailingContent = {
            ZarinaTextFieldDefaults.ClearButton(
                isVisible = nameTextFieldState.text.isNotEmpty(),
                onClick = {
                    nameTextFieldState.clearText()
                    if (nameFocusRequester.tryRequestFocus()) {
                        keyboardController?.show()
                    }
                },
            )
        },
        keyboardOptions = remember {
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .focusRequester(nameFocusRequester)
            .semantics { contentType = ContentType.PersonFirstName },
    )
    Spacer(modifier = Modifier.height(16.dp))

    val formattedBirthDate = if (birthDateEpochMillis != null) {
        rememberFormattedLocalDate(
            localDate = remember(birthDateEpochMillis) {
                LocalDateUtil.fromEpochMillis(birthDateEpochMillis, ZoneOffset.UTC)
            },
            formatterPattern = DateTimeUtils.DATE_FORMAT_PATTERN,
        )
    } else ""

    ZarinaTextField(
        value = formattedBirthDate.uppercase(),
        onValueChanged = {},
        isEnabled = false,
        isError = isBirthDateInvalid,
        label = {
            val label = if (formattedBirthDate.isNotEmpty()) {
                stringResource(RCommon.string.res_birth_date)
            } else ""

            Text(text = label.uppercase())
        },
        placeholder = {
            Text(text = stringResource(RCommon.string.res_birth_date).uppercase())
        },
        colors = ZarinaTextFieldDefaults.colorsIgnoringDisabled(),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBirthDateClicked)
            .padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(16.dp))

    ZarinaTextField(
        state = emailTextFieldState,
        isError = isEmailInvalid,
        label = {
            val label = if (emailTextFieldState.text.isNotEmpty()) {
                stringResource(RCommon.string.res_email)
            } else ""

            Text(text = label.uppercase())
        },
        placeholder = {
            Text(text = stringResource(RCommon.string.res_email).uppercase())
        },
        innerTrailingContent = {
            ZarinaTextFieldDefaults.ClearButton(
                isVisible = emailTextFieldState.text.isNotEmpty(),
                onClick = {
                    emailTextFieldState.clearText()
                    if (emailFocusRequester.tryRequestFocus()) {
                        keyboardController?.show()
                    }
                },
            )
        },
        keyboardOptions = remember {
            KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .focusRequester(emailFocusRequester)
            .semantics { contentType = ContentType.NewUsername },
    )
    Spacer(modifier = Modifier.height(16.dp))

    ZarinaPhoneTextField(
        state = phoneTextFieldState,
        isError = isPhoneInvalid,
        keyboardOptions = remember {
            KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .semantics { contentType = ContentType.PhoneNumber },
    )
    Spacer(modifier = Modifier.height(16.dp))

    ZarinaPasswordTextField(
        state = passwordTextFieldState,
        isError = isPasswordInvalid,
        keyboardOptions = remember {
            KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .semantics { contentType = ContentType.NewPassword },
    )
}

@Suppress("UnusedReceiverParameter")
@Composable
private fun ColumnScope.SubscriptionSetup(
    receiveEmails: Boolean,
    onReceiveEmailsChanged: (Boolean) -> Unit,
    receiveSms: Boolean,
    onReceiveSmsChanged: (Boolean) -> Unit,
) {
    ZarinaItem(
        onClick = { onReceiveEmailsChanged(!receiveEmails) },
        startContent = {
            Text(
                text = stringResource(R.string.sign_up_receive_news_by_email).uppercase(),
                style = UiKitTheme2.typography.body,
            )
        },
        endContent = {
            ZarinaSwitch(
                isChecked = receiveEmails,
                onCheckedChanged = onReceiveEmailsChanged,
            )
        },
        modifier = Modifier.fillMaxWidth(),
    )

    ZarinaDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )

    ZarinaItem(
        onClick = { onReceiveSmsChanged(!receiveSms) },
        startContent = {
            Text(
                text = stringResource(R.string.sign_up_receive_sms_notifications).uppercase(),
                style = UiKitTheme2.typography.body,
                modifier = Modifier.weight(1f),
            )
        },
        endContent = {
            ZarinaSwitch(
                isChecked = receiveSms,
                onCheckedChanged = onReceiveSmsChanged,
            )
        },
        modifier = Modifier.fillMaxWidth(),
    )
}
