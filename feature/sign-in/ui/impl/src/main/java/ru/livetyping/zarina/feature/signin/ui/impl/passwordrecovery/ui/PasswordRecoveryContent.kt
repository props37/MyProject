package ru.livetyping.zarina.feature.signin.ui.impl.passwordrecovery.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.signin.ui.impl.R

@Composable
internal fun PasswordRecoveryContent(
    state: ru.livetyping.zarina.feature.signin.ui.impl.passwordrecovery.model.PasswordRecoveryState,
    onRequestPasswordRecoveryClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val emailFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        withFrameMillis {}
        emailFocusRequester.tryRequestFocus()
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.sign_in_enter_email_specified_during_registration).uppercase(),
            style = UiKitTheme2.typography.bodyBold,
            color = UiKitTheme2.colors.mainBlack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sign_in_we_will_send_link_to_reset_password_to_specified_email).uppercase(),
            style = UiKitTheme2.typography.body,
            color = UiKitTheme2.colors.mainBlack,
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

                Text(text = label.uppercase())
            },
            placeholder = {
                Text(text = stringResource(ru.livetyping.zarina.core.resource.R.string.res_email).uppercase())
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
                    imeAction = ImeAction.Done,
                )
            },
            onKeyboardAction = { defaultAction ->
                defaultAction()
                onRequestPasswordRecoveryClicked()
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

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
    }
}
