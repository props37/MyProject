package ru.livetyping.zarina.presentation.screen.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPasswordTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPhoneNumberTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.screen.signin.SignInViewModel.SignInType
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.autofill.autofill
import ru.livetyping.zarina.util.compose.navigationBarsOrIme
import ru.livetyping.zarina.util.compose.text.rememberStringWithLinks
import ru.livetyping.zarina.util.compose.tryRequestFocus

object SignInScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.sign_in_to_account),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun SignInTypeTabRow(
        signInTypes: ImmutableList<SignInType>,
        currentSignInType: SignInType,
        onSignInTypeChanged: (SignInType) -> Unit,
        signInTypePagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTabRow(
            selectedTabIndex = signInTypePagerState.currentPage,
            modifier = modifier,
        ) {
            signInTypes.forEach { type ->
                val textResId = when (type) {
                    SignInType.EMAIL -> R.string.by_email
                    SignInType.PHONE -> R.string.by_phone
                }

                ZarinaTab(
                    text = stringResource(textResId),
                    onClick = { onSignInTypeChanged(type) },
                    isSelected = type == currentSignInType,
                )
            }
        }
    }

    @Composable
    fun SignInTypePager(
        signInTypes: ImmutableList<SignInType>,
        signInTypePagerState: PagerState,
        email: String,
        onEmailChanged: (String) -> Unit,
        isEmailInvalid: Boolean,
        password: String,
        onPasswordChanged: (String) -> Unit,
        isPasswordInvalid: Boolean,
        phone: String,
        onPhoneChanged: (String) -> Unit,
        isPhoneInvalid: Boolean,
        onSignInClicked: () -> Unit,
        isSignInButtonLoading: Boolean,
        onForgotPasswordClicked: () -> Unit,
        onSignUpClicked: () -> Unit,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = signInTypePagerState,
            verticalAlignment = Alignment.Top,
            modifier = modifier,
        ) { page ->
            val signInType = signInTypes[page]
            when (signInType) {
                SignInType.EMAIL -> {
                    SignInByEmail(
                        email = email,
                        onEmailChanged = onEmailChanged,
                        isEmailInvalid = isEmailInvalid,
                        password = password,
                        onPasswordChanged = onPasswordChanged,
                        isPasswordInvalid = isPasswordInvalid,
                        onSignInClicked = onSignInClicked,
                        isSignInButtonLoading = isSignInButtonLoading,
                        onForgotPasswordClicked = onForgotPasswordClicked,
                        onSignUpClicked = onSignUpClicked,
                        onUrlClicked = onUrlClicked,
                    )
                }

                SignInType.PHONE -> {
                    SignInByPhone(
                        phone = phone,
                        onPhoneChanged = onPhoneChanged,
                        isPhoneInvalid = isPhoneInvalid,
                        onSignInClicked = onSignInClicked,
                        isSignInButtonLoading = isSignInButtonLoading,
                        onSignUpClicked = onSignUpClicked,
                        onUrlClicked = onUrlClicked,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun SignInByEmail(
        email: String,
        onEmailChanged: (String) -> Unit,
        isEmailInvalid: Boolean,
        password: String,
        onPasswordChanged: (String) -> Unit,
        isPasswordInvalid: Boolean,
        onSignInClicked: () -> Unit,
        isSignInButtonLoading: Boolean,
        onForgotPasswordClicked: () -> Unit,
        onSignUpClicked: () -> Unit,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val emailFocusRequester = remember { FocusRequester() }
        val passwordFocusRequester = remember { FocusRequester() }
        var lastFocusTarget by rememberSaveable {
            mutableStateOf(SignInByEmailFocusTarget.Email)
        }

        LifecycleStartEffect(Unit) {
            val focusRequester = when (lastFocusTarget) {
                SignInByEmailFocusTarget.Email -> emailFocusRequester
                SignInByEmailFocusTarget.Password -> passwordFocusRequester
            }
            focusRequester.tryRequestFocus()
            onStopOrDispose {}
        }

        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(TopPadding))

            ZarinaTextField(
                value = email,
                onValueChanged = onEmailChanged,
                isError = isEmailInvalid,
                label = { Text(text = stringResource(R.string.email)) },
                placeholder = {
                    Text(text = stringResource(R.string.email_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = email.isNotEmpty(),
                        onClick = {
                            onEmailChanged("")
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
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(emailFocusRequester)
                    .onFocusChanged {
                        if (it.isFocused) lastFocusTarget = SignInByEmailFocusTarget.Email
                    }
                    .autofill(
                        autofillType = AutofillType.EmailAddress,
                        onFilled = { onEmailChanged(it) },
                    ),
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
                    .padding(horizontal = 16.dp)
                    .focusRequester(passwordFocusRequester)
                    .onFocusChanged {
                        if (it.isFocused) lastFocusTarget = SignInByEmailFocusTarget.Password
                    }
                    .autofill(
                        autofillType = AutofillType.Password,
                        onFilled = { onPasswordChanged(it) },
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
                    text = stringResource(R.string.forgot_password_question).uppercase(),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SignInBlock(
                isSignInButtonLoading = isSignInButtonLoading,
                onSignInClicked = onSignInClicked,
                onSignUpClicked = onSignUpClicked,
                onUrlClicked = onUrlClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun SignInByPhone(
        phone: String,
        onPhoneChanged: (String) -> Unit,
        isPhoneInvalid: Boolean,
        onSignInClicked: () -> Unit,
        isSignInButtonLoading: Boolean,
        onSignUpClicked: () -> Unit,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val focusRequester = remember { FocusRequester() }
        LifecycleStartEffect(Unit) {
            focusRequester.tryRequestFocus()
            onStopOrDispose {}
        }

        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(32.dp))

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester)
                    .autofill(
                        autofillType = AutofillType.PhoneNumber,
                        onFilled = { onPhoneChanged(it) },
                    ),
            )

            Spacer(modifier = Modifier.height(32.dp))

            SignInBlock(
                isSignInButtonLoading = isSignInButtonLoading,
                onSignInClicked = onSignInClicked,
                onSignUpClicked = onSignUpClicked,
                onUrlClicked = onUrlClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }

    @Composable
    private fun SignInBlock(
        isSignInButtonLoading: Boolean,
        onSignInClicked: () -> Unit,
        onSignUpClicked: () -> Unit,
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaButton(
                onClick = onSignInClicked,
                isLoading = isSignInButtonLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.sign_in).uppercase())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Policies(onUrlClicked = onUrlClicked)
            
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.do_not_have_account_yet_question),
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
                Text(text = stringResource(R.string.sign_up).uppercase())
            }

            Spacer(modifier = Modifier.height(20.dp))
            val navigationBarsOrImeBottomPadding =
                WindowInsets.navigationBarsOrIme.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarsOrImeBottomPadding))
        }
    }

    @Composable
    private fun Policies(
        onUrlClicked: (Url) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val privacyPolicy = stringResource(R.string.sign_in_policies_privacy_policy)
        val yandexCaptchaPrivacy = stringResource(R.string.sign_up_yandex_captcha_policies_terms)

        val privacyPolicyUrl = stringResource(R.string.privacy_policy_url)
        val yandexCaptchaTermsUrl = stringResource(R.string.yandex_captcha_terms_policy_url)

        val substringToUrl = remember(
            privacyPolicy,
            yandexCaptchaPrivacy,
            privacyPolicyUrl,
            yandexCaptchaTermsUrl,
        ) {
            mapOf(
                privacyPolicy to privacyPolicyUrl,
                yandexCaptchaPrivacy to yandexCaptchaTermsUrl,
            )
        }
        val stringWithLinks = rememberStringWithLinks(
            baseString = stringResource(R.string.sign_in_policies),
            substringToUrl = substringToUrl,
            urlStyle = UiKitTheme.typography.footnote.regular.toSpanStyle(),
            onUrlClicked = { onUrlClicked(Url(it)) },
        )

        Text(
            text = stringWithLinks,
            style = UiKitTheme.typography.footnote.light,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = modifier,
        )
    }

    private val TopPadding: Dp get() = 32.dp

    private enum class SignInByEmailFocusTarget { Email, Password }
}
