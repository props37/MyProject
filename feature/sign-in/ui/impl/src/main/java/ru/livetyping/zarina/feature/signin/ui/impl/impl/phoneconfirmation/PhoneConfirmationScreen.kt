package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import ru.livetyping.zarina.core.uicomponent.otp.SmsOtp
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaDialog
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaEvent
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaState
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.component.PhoneConfirmationTopBar
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.model.PhoneConfirmationEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.model.PhoneConfirmationState

@Composable
internal fun PhoneConfirmationScreen(
    navActions: PhoneConfirmationNavActions,
    viewModel: PhoneConfirmationViewModel = hiltViewModel(),
) {
    val state by viewModel.phoneConfirmationState.collectAsStateWithLifecycle()
    val yandexCaptchaState by viewModel.yandexCaptchaState.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        onEvent = viewModel::onPhoneConfirmationEvent,
        yandexCaptchaState = yandexCaptchaState,
        onYandexCaptchaEvent = viewModel::onYandexCaptchaEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    state: PhoneConfirmationState,
    onEvent: (PhoneConfirmationEvent) -> Unit,
    yandexCaptchaState: YandexCaptchaState,
    onYandexCaptchaEvent: (YandexCaptchaEvent) -> Unit,
    sideEffects: Flow<PhoneConfirmationSideEffect>,
    navActions: PhoneConfirmationNavActions,
) {
    PhoneConfirmationScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Box {
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
            PhoneConfirmationTopBar(onBackClicked = { onEvent(PhoneConfirmationEvent.BackClicked) })

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
                    isRequestNewOtpButtonLoading = state.isRequestNewOtpButtonLoading,
                    onOtpEntered = { onEvent(PhoneConfirmationEvent.OtpEntered) },
                    onRequestNewOtpClicked = {
                        onEvent(PhoneConfirmationEvent.RequestNewOtpClicked)
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

        YandexCaptchaDialog(
            state = yandexCaptchaState,
            onEvent = onYandexCaptchaEvent,
        )
    }
}

private const val FocusRequestDelay = 100L
