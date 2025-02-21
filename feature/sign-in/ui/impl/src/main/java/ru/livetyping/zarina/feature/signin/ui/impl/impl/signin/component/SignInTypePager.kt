package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.autofill.autofill
import ru.livetyping.zarina.core.uicompose.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextFieldDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaPhoneTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInType
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SignInTypePager(
    signInTypes: ImmutableList<SignInType>,
    pagerState: PagerState,
    signInState: SignInState,
    onSignInEvent: (SignInEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top,
        key = { page -> signInTypes[page] },
        modifier = modifier,
    ) { page ->
        val signInType = signInTypes[page]
        when (signInType) {
            SignInType.EMAIL -> {
                SignInByEmail(
                    emailTextFieldState = signInState.emailTextFieldState,
                    isEmailInvalid = signInState.isEmailInvalid,
                    passwordTextFieldState = signInState.passwordTextFieldState,
                    isPasswordInvalid = signInState.isPasswordInvalid,
                    onForgotPasswordClicked = { onSignInEvent(SignInEvent.ForgotPasswordClicked) },
                    isSignInButtonLoading = signInState.isSignInButtonLoading,
                    onSignInClicked = { onSignInEvent(SignInEvent.SignInClicked) },
                    onSignUpClicked = { onSignInEvent(SignInEvent.SignUpClicked) },
                    windowInsetsProvider = windowInsetsProvider,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            SignInType.PHONE -> {
                SignInByPhone(
                    phoneTextFieldState = signInState.phoneTextFieldState,
                    isPhoneInvalid = signInState.isPhoneInvalid,
                    isSignInButtonLoading = signInState.isSignInButtonLoading,
                    onSignInClicked = { onSignInEvent(SignInEvent.SignInClicked) },
                    onSignUpClicked = { onSignInEvent(SignInEvent.SignUpClicked) },
                    windowInsetsProvider = windowInsetsProvider,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun SignInByEmail(
    emailTextFieldState: TextFieldState,
    isEmailInvalid: Boolean,
    passwordTextFieldState: TextFieldState,
    isPasswordInvalid: Boolean,
    onForgotPasswordClicked: () -> Unit,
    isSignInButtonLoading: Boolean,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    var lastFocusTarget by rememberSaveable {
        mutableStateOf(SignInByEmailFocusTarget.Email)
    }

    LaunchedEffect(Unit) {
        withFrameMillis {}
        val focusRequester = when (lastFocusTarget) {
            SignInByEmailFocusTarget.Email -> emailFocusRequester
            SignInByEmailFocusTarget.Password -> passwordFocusRequester
        }
        focusRequester.tryRequestFocus()
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(TopPadding))

        ZarinaTextField(
            state = emailTextFieldState,
            isError = isEmailInvalid,
            label = {
                val labelResId = if (emailTextFieldState.text.isNotEmpty()) {
                    stringResource(RCommon.string.res_email)
                } else ""

                Text(text = labelResId)
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
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(emailFocusRequester)
                .onFocusChanged {
                    if (it.isFocused) lastFocusTarget = SignInByEmailFocusTarget.Email
                }
                .autofill(
                    autofillType = AutofillType.EmailAddress,
                    onFilled = {
                        emailTextFieldState.setTextAndPlaceCursorAtEnd(it)
                    },
                ),
        )
        Spacer(modifier = Modifier.height(16.dp))

        ZarinaPasswordTextField(
            state = passwordTextFieldState,
            isError = isPasswordInvalid,
            keyboardOptions = remember {
                ZarinaPasswordTextFieldDefaults.KeyboardOptions.copy(
                    imeAction = ImeAction.Done,
                )
            },
            onKeyboardAction = { defaultAction ->
                defaultAction()
                onSignInClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(passwordFocusRequester)
                .onFocusChanged {
                    if (it.isFocused) lastFocusTarget = SignInByEmailFocusTarget.Password
                }
                .autofill(
                    autofillType = AutofillType.Password,
                    onFilled = {
                        passwordTextFieldState.setTextAndPlaceCursorAtEnd(it)
                    },
                ),
        )

        ZarinaButton(
            onClick = onForgotPasswordClicked,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.backlessColors(),
            contentPadding = PaddingValues(vertical = 8.dp),
            indication = null,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.sign_in_forgot_password_question).uppercase(),
            )
        }

        Spacer(modifier = Modifier.height(SignInBottomBlockTopPadding))

        SignInBottomBlock(
            isSignInButtonLoading = isSignInButtonLoading,
            onSignInClicked = onSignInClicked,
            onSignUpClicked = onSignUpClicked,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        BottomSpacer(windowInsets = windowInsetsProvider())
    }
}

@Composable
private fun SignInByPhone(
    phoneTextFieldState: TextFieldState,
    isPhoneInvalid: Boolean,
    isSignInButtonLoading: Boolean,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        withFrameMillis {}
        focusRequester.tryRequestFocus()
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(TopPadding))

        ZarinaPhoneTextField(
            state = phoneTextFieldState,
            isError = isPhoneInvalid,
            keyboardOptions = remember {
                KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done,
                )
            },
            onKeyboardAction = { defaultAction ->
                defaultAction()
                onSignInClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(focusRequester)
                .autofill(
                    autofillType = AutofillType.PhoneNumber,
                    onFilled = {
                        phoneTextFieldState.setTextAndPlaceCursorAtEnd(it)
                    },
                ),
        )

        Spacer(modifier = Modifier.height(SignInBottomBlockTopPadding))

        SignInBottomBlock(
            isSignInButtonLoading = isSignInButtonLoading,
            onSignInClicked = onSignInClicked,
            onSignUpClicked = onSignUpClicked,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        BottomSpacer(windowInsets = windowInsetsProvider())
    }
}

@Composable
private fun SignInBottomBlock(
    isSignInButtonLoading: Boolean,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ZarinaButton(
            onClick = onSignInClicked,
            isLoading = isSignInButtonLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.sign_in_sign_in).uppercase())
        }
        Spacer(modifier = Modifier.height(16.dp))

        Policies()
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.sign_in_do_not_have_account_yet_question),
            style = UiKitTheme.typography.tertiary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))

        ZarinaButton(
            onClick = onSignUpClicked,
            colors = ZarinaButtonDefaults.outlineColors(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.sign_in_sign_up).uppercase())
        }
    }
}

@Composable
private fun Policies(
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    val baseString = stringResource(R.string.sign_in_policies)
    val privacyPolicy = stringResource(R.string.sign_in_policies_privacy_policy)
    val yandexCaptchaTermsPolicy = stringResource(R.string.sign_in_policies_yandex_captcha_terms_policy)
    val privacyPolicyUrl = stringResource(RCommon.string.res_zarina_privacy_policy_url)
    val yandexCaptchaTermsPolicyUrl = stringResource(RCommon.string.res_yandex_captcha_terms_policy_url)

    val substringToUrl = remember(
        privacyPolicy,
        yandexCaptchaTermsPolicy,
        privacyPolicyUrl,
        yandexCaptchaTermsPolicyUrl,
    ) {
        mapOf(
            privacyPolicy to privacyPolicyUrl,
            yandexCaptchaTermsPolicy to yandexCaptchaTermsPolicyUrl,
        )
    }

    val stringWithLinks = rememberAnnotatedStringWithLinks(
        baseString = baseString,
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

@Composable
private fun BottomSpacer(
    windowInsets: WindowInsets,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsets))
    }
}

private val TopPadding: Dp get() = 32.dp
private val SignInBottomBlockTopPadding: Dp get() = 32.dp

private const val FocusRequesterDelayMillis = 100L

private enum class SignInByEmailFocusTarget { Email, Password }
