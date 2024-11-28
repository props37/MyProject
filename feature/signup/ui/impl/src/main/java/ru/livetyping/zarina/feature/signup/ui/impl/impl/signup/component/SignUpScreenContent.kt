package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.livetyping.zarina.core.kotlinutil.LocalDateUtil
import ru.livetyping.zarina.core.uicommon.DateTimeUtils
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.autofill.autofill
import ru.livetyping.zarina.core.uicompose.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uicompose.rememberFormattedLocalDate
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaPolicies
import ru.livetyping.zarina.core.uikit.checkbox.ZarinaCheckbox
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.switchh.ZarinaSwitch
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaPhoneTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.signup.ui.impl.R
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpEvent
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpState
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

        val windowInsetsBottomPadding =
            windowInsetsProvider().asPaddingValues().calculateBottomPadding()
        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.height(windowInsetsBottomPadding))
    }
}

@Suppress("UnusedReceiverParameter")
@OptIn(ExperimentalComposeUiApi::class)
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
    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(FocusRequesterDelayMillis)
        nameFocusRequester.tryRequestFocus()
    }

    ZarinaTextField(
        state = nameTextFieldState,
        isError = isNameInvalid,
        label = {
            val label = if (nameTextFieldState.text.isNotEmpty()) {
                stringResource(RCommon.string.res_first_name)
            } else ""

            Text(text = label)
        },
        placeholder = {
            Text(text = stringResource(RCommon.string.res_first_name))
        },
        innerTrailingContent = {
            ZarinaTextFieldDefaults.ClearButton(
                isVisible = nameTextFieldState.text.isNotEmpty(),
                onClick = {
                    nameTextFieldState.clearText()
                    nameFocusRequester.tryRequestFocus()
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
            .autofill(
                autofillType = AutofillType.PersonFirstName,
                onFilled = nameTextFieldState::setTextAndPlaceCursorAtEnd,
            ),
    )
    Spacer(modifier = Modifier.height(16.dp))

    val formattedBirthDate = if (birthDateEpochMillis != null) {
        rememberFormattedLocalDate(
            localDate = remember(birthDateEpochMillis) {
                LocalDateUtil.fromMillis(birthDateEpochMillis)
            },
            formatterPattern = DateTimeUtils.DATE_FORMAT_PATTERN,
        )
    } else ""

    ZarinaTextField(
        value = formattedBirthDate,
        onValueChanged = {},
        isEnabled = false,
        isError = isBirthDateInvalid,
        label = {
            val label = if (formattedBirthDate.isNotEmpty()) {
                stringResource(RCommon.string.res_birth_date)
            } else ""

            Text(text = label)
        },
        placeholder = {
            Text(text = stringResource(RCommon.string.res_birth_date))
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

            Text(text = label)
        },
        placeholder = {
            Text(text = stringResource(RCommon.string.res_email))
        },
        innerTrailingContent = {
            ZarinaTextFieldDefaults.ClearButton(
                isVisible = emailTextFieldState.text.isNotEmpty(),
                onClick = {
                    emailTextFieldState.clearText()
                    emailFocusRequester.tryRequestFocus()
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
            .autofill(
                autofillType = AutofillType.EmailAddress,
                onFilled = emailTextFieldState::setTextAndPlaceCursorAtEnd,
            ),
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
            .autofill(
                autofillType = AutofillType.PhoneNumber,
                onFilled = phoneTextFieldState::setTextAndPlaceCursorAtEnd,
            ),
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
            .autofill(
                autofillType = AutofillType.NewPassword,
                onFilled = passwordTextFieldState::setTextAndPlaceCursorAtEnd,
            ),
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
                text = stringResource(R.string.sign_up_receive_news_by_email),
                style = UiKitTheme.typography.secondary.light,
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
                text = stringResource(R.string.sign_up_receive_sms_notifications),
                style = UiKitTheme.typography.secondary.light,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Policies(
    areAccepted: Boolean,
    onAcceptedChanged: (Boolean) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        PoliciesText(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            ZarinaCheckbox(
                isChecked = areAccepted,
                onCheckedChanged = onAcceptedChanged,
                isError = isError,
            )
        }
    }
}

@Composable
private fun PoliciesText(
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    val privacy = stringResource(R.string.sign_up_policies_privacy)
    val onlineStore = stringResource(R.string.sign_up_policies_online_store)
    val loyalty = stringResource(R.string.sign_up_policies_loyalty)

    val privacyUrl = stringResource(RCommon.string.res_zarina_privacy_policy_url)
    val onlineStoreUrl = stringResource(RCommon.string.res_zarina_online_store_policy_url)
    val loyaltyUrl = stringResource(RCommon.string.res_zarina_loyalty_policy_url)

    val substringToUrl = remember(
        privacy,
        onlineStore,
        loyalty,
        privacyUrl,
        onlineStoreUrl,
        loyaltyUrl,
    ) {
        mapOf(
            privacy to privacyUrl,
            onlineStore to onlineStoreUrl,
            loyalty to loyaltyUrl,
        )
    }
    val stringWithLinks = rememberAnnotatedStringWithLinks(
        baseString = stringResource(R.string.sign_up_policies),
        substringToUrl = substringToUrl,
        urlStyle = UiKitTheme.typography.footnote.regular.toSpanStyle(),
        onUrlClicked = currentContext::openUrlInCustomTabs,
    )

    Text(
        text = stringWithLinks,
        style = UiKitTheme.typography.footnote.light,
        color = UiKitTheme.colors.text.general.regular.default,
        modifier = modifier,
    )
}

private const val FocusRequesterDelayMillis = 100L
