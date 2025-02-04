package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchange

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchange.component.EmailChangeTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchange.model.EmailChangeEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchange.model.EmailChangeState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun EmailChangeScreen(
    navActions: EmailChangeNavActions,
    viewModel: EmailChangeViewModel = hiltViewModel(),
) {
    val state by viewModel.emailChangeState.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        onEvent = viewModel::onEmailChangeEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    state: EmailChangeState,
    onEvent: (EmailChangeEvent) -> Unit,
    sideEffects: Flow<EmailChangeSideEffect>,
    navActions: EmailChangeNavActions,
) {
    EmailChangeScreenBehavior(
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
        EmailChangeTopBar(onBackClicked = { onEvent(EmailChangeEvent.BackClicked) })

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.profile_enter_new_email),
                style = UiKitTheme.typography.secondary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(focusRequester) {
                delay(FocusRequestDelay)
                focusRequester.tryRequestFocus()
            }

            ZarinaTextField(
                state = state.emailTextFieldState,
                isError = state.isEmailInvalid,
                label = {
                    val label = if (state.emailTextFieldState.text.isNotEmpty()) {
                        stringResource(RCommon.string.res_email)
                    } else ""

                    Text(text = label)
                },
                placeholder = {
                    Text(text = stringResource(RCommon.string.res_email))
                },
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    )
                },
                onKeyboardAction = { defaultAction ->
                    defaultAction()
                    onEvent(EmailChangeEvent.ChangeEmailClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester)
                    .autofill(
                        autofillType = AutofillType.EmailAddress,
                        onFilled = { state.emailTextFieldState.setTextAndPlaceCursorAtEnd(it) },
                    ),
            )

            Spacer(modifier = Modifier.height(32.dp))

            ZarinaButton(
                onClick = { onEvent(EmailChangeEvent.ChangeEmailClicked) },
                isLoading = state.isChangeEmailButtonLoading,
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
