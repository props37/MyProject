package ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.autofill.autofill
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaPasswordTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.component.PasswordChangingTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.model.PasswordChangingEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.passwordchanging.model.PasswordChangingState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun PasswordChangingScreen(
    navActions: PasswordChangingNavActions,
    viewModel: PasswordChangingViewModel = hiltViewModel(),
) {
    val state by viewModel.passwordChangingState.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        onEvent = viewModel::onPasswordChangingEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ScreenContent(
    state: PasswordChangingState,
    onEvent: (PasswordChangingEvent) -> Unit,
    sideEffects: Flow<PasswordChangingSideEffect>,
    navActions: PasswordChangingNavActions,
) {
    PasswordChangingScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        PasswordChangingTopBar(
            onBackClicked = { onEvent(PasswordChangingEvent.BackClicked) },
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            val oldPasswordFocusRequester = remember { FocusRequester() }
            LaunchedEffect(oldPasswordFocusRequester) {
                delay(FocusRequestDelay)
                oldPasswordFocusRequester.tryRequestFocus()
            }

            ZarinaPasswordTextField(
                state = state.oldPasswordTextFieldState,
                isError = state.isOldPasswordInvalid,
                label = {
                    val label = if (state.oldPasswordTextFieldState.text.isNotEmpty()) {
                        stringResource(RCommon.string.res_old_password)
                    } else ""

                    Text(text = label)
                },
                placeholder = {
                    Text(text = stringResource(RCommon.string.res_old_password))
                },
                keyboardOptions = remember {
                    ZarinaPasswordTextFieldDefaults.KeyboardOptions
                        .copy(imeAction = ImeAction.Next)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(oldPasswordFocusRequester)
                    .autofill(
                        autofillType = AutofillType.Password,
                        onFilled = state.oldPasswordTextFieldState::setTextAndPlaceCursorAtEnd,
                    ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZarinaPasswordTextField(
                state = state.newPasswordTextFieldState,
                isError = state.isNewPasswordInvalid,
                label = {
                    val label = if (state.newPasswordTextFieldState.text.isNotEmpty()) {
                        stringResource(RCommon.string.res_new_password)
                    } else ""

                    Text(text = label)
                },
                placeholder = {
                    Text(text = stringResource(RCommon.string.res_new_password))
                },
                onKeyboardAction = { defaultAction ->
                    defaultAction()
                    onEvent(PasswordChangingEvent.ChangePasswordClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .autofill(
                        autofillType = AutofillType.NewPassword,
                        onFilled = state.newPasswordTextFieldState::setTextAndPlaceCursorAtEnd,
                    ),
            )

            Spacer(modifier = Modifier.height(32.dp))

            ZarinaButton(
                onClick = { onEvent(PasswordChangingEvent.ChangePasswordClicked) },
                isLoading = state.isChangePasswordButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(RCommon.string.res_change).uppercase())
            }

            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

private const val FocusRequestDelay = 100L
