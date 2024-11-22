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
import androidx.compose.foundation.text.input.placeCursorAtEnd
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import ru.livetyping.zarina.core.uicompose.autofill.autofill
import ru.livetyping.zarina.core.uicompose.clear
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextFieldDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInType
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SignInTypePager(
    signInTypeSelectorState: TabRowState<SignInType>,
    pagerState: PagerState,
    emailTextFieldState: TextFieldState,
    isEmailInvalid: Boolean,
    passwordTextFieldState: TextFieldState,
    isPasswordInvalid: Boolean,
    phoneTextFieldState: TextFieldState,
    isPhoneInvalid: Boolean,
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
                    emailTextFieldState = emailTextFieldState,
                    isEmailInvalid = isEmailInvalid,
                    passwordTextFieldState = passwordTextFieldState,
                    isPasswordInvalid = isPasswordInvalid,
                    phoneTextFieldState = phoneTextFieldState,
                    isPhoneInvalid = isPhoneInvalid,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            SignInType.PHONE -> TODO()
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
    phoneTextFieldState: TextFieldState,
    isPhoneInvalid: Boolean,
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
        Spacer(modifier = Modifier.height(32.dp))

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
                            clear()
                            append(it)
                            placeCursorAtEnd()
                        }
                    },
                ),
        )
        Spacer(modifier = Modifier.height(16.dp))

        ZarinaPasswordTextField(
            state = passwordTextFieldState,
            isError = isPasswordInvalid,
            label = {
                val labelResId = if (passwordTextFieldState.text.isNotEmpty()) {
                    stringResource(RCommon.string.res_password)
                } else ""

                Text(text = labelResId)
            },
            placeholder = {
                Text(text = stringResource(RCommon.string.res_password))
            },
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
                            clear()
                            append(it)
                            placeCursorAtEnd()
                        }
                    },
                ),
        )

        // TODO: [Top] Implement
    }
}

private enum class SignInByEmailFocusTarget { Email, Password }
