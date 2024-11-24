package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import ru.livetyping.zarina.core.uicompose.autofill.autofill
import ru.livetyping.zarina.core.uicompose.setTextAndPlaceCursorAtEnd
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextFieldDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaPhoneTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInType
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SignInTypePager(
    signInTypeSelectorState: TabRowState<SignInType>,
    pagerState: PagerState,
    signInState: SignInState,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val signInType = signInTypeSelectorState.tabs[page]
        when (signInType) {
            SignInType.EMAIL -> {
                SignInByEmail(
                    emailTextFieldState = signInState.emailTextFieldState,
                    isEmailInvalid = signInState.isEmailInvalid,
                    passwordTextFieldState = signInState.passwordTextFieldState,
                    isPasswordInvalid = signInState.isPasswordInvalid,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            SignInType.PHONE -> {
                SignInByPhone(
                    phoneTextFieldState = signInState.phoneTextFieldState,
                    isPhoneInvalid = signInState.isPhoneInvalid,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SignInByEmail(
    emailTextFieldState: TextFieldState,
    isEmailInvalid: Boolean,
    passwordTextFieldState: TextFieldState,
    isPasswordInvalid: Boolean,
    modifier: Modifier = Modifier,
) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    var lastFocusTarget by remember {
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
                        emailTextFieldState.edit {
                            setTextAndPlaceCursorAtEnd(it)
                        }
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
                        passwordTextFieldState.edit {
                            setTextAndPlaceCursorAtEnd(it)
                        }
                    },
                ),
        )

        // TODO: [Top] Implement
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SignInByPhone(
    phoneTextFieldState: TextFieldState,
    isPhoneInvalid: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LifecycleStartEffect(Unit) {
        focusRequester.tryRequestFocus()
        onStopOrDispose {}
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(focusRequester)
                .autofill(
                    autofillType = AutofillType.PhoneNumber,
                    onFilled = {
                        phoneTextFieldState.edit {
                            setTextAndPlaceCursorAtEnd(it)
                        }
                    },
                ),
        )

        // TODO: [Top] Implement
    }
}

private val TopPadding: Dp get() = 32.dp

private enum class SignInByEmailFocusTarget { Email, Password }
