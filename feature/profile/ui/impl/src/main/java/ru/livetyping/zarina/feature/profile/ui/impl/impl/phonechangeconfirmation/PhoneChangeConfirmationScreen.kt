package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.otp.SmsOtp
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.component.PhoneChangeConfirmationTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationState

@Composable
internal fun PhoneChangeConfirmationScreen(
    navActions: PhoneChangeConfirmationNavActions,
    viewModel: PhoneChangeConfirmationViewModel = hiltViewModel(),
) {
    val state by viewModel.phoneChangeConfirmationState.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        onEvent = viewModel::onPhoneChangeConfirmationEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    state: PhoneChangeConfirmationState,
    onEvent: (PhoneChangeConfirmationEvent) -> Unit,
    sideEffects: Flow<PhoneChangeConfirmationSideEffect>,
    navActions: PhoneChangeConfirmationNavActions,
) {
    PhoneChangeConfirmationScreenBehavior(
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
        PhoneChangeConfirmationTopBar(
            onBackClicked = { onEvent(PhoneChangeConfirmationEvent.BackClicked) },
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                delay(FocusRequestDelay)
                focusRequester.tryRequestFocus()
            }

            SmsOtp(
                otpState = state.otpState,
                phone = state.phone,
                onOtpEntered = { onEvent(PhoneChangeConfirmationEvent.OtpEntered) },
                onRequestNewOtpClicked = {
                    onEvent(PhoneChangeConfirmationEvent.RequestNewOtpClicked)
                },
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )

            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

private const val FocusRequestDelay = 100L
