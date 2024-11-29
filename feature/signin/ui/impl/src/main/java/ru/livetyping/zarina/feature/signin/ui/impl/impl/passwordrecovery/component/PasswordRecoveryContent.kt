package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.model.PasswordRecoveryState

@Composable
internal fun PasswordRecoveryContent(
    state: PasswordRecoveryState,
    onRequestPasswordRecoveryClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    val emailFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        emailFocusRequester.tryRequestFocus()
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.sign_in_enter_email_specified_during_registration),
            style = UiKitTheme.typography.secondary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sign_in_we_will_send_link_to_reset_password_to_specified_email),
            style = UiKitTheme.typography.tertiary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))

        val emailTextFieldState = state.emailTextFieldState

        ZarinaTextField(
            state = emailTextFieldState,
            isError = state.isEmailInvalid,
            label = {
                val label = if (emailTextFieldState.text.isNotEmpty()) {
                    stringResource(ru.livetyping.zarina.core.resource.R.string.res_email)
                } else ""

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(ru.livetyping.zarina.core.resource.R.string.res_email))
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
                    imeAction = ImeAction.Done,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(emailFocusRequester),
        )

        Spacer(modifier = Modifier.height(48.dp))

        ZarinaButton(
            onClick = onRequestPasswordRecoveryClicked,
            isLoading = state.isSendButtonLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(text = stringResource(ru.livetyping.zarina.core.resource.R.string.res_send).uppercase())
        }

        val windowInsetsBottomPadding =
            windowInsetsProvider().asPaddingValues().calculateBottomPadding()
        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.height(windowInsetsBottomPadding))
    }
}
