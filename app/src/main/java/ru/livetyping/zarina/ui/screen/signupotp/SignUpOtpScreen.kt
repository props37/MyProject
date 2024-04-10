package ru.livetyping.zarina.ui.screen.signupotp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.ui.common.component.SmsOtp
import ru.livetyping.zarina.ui.common.otp.OtpResendState
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.signupotp.SignUpOtpScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.signupotp.SignUpOtpViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.tryRequestFocus

@Composable
fun SignUpOtpScreen(
    navigate: (SignUpOtpScreenAction) -> Unit,
    viewModel: SignUpOtpViewModel = hiltViewModel(),
) {
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val otp by viewModel.otp.collectAsStateWithLifecycle()
    val isOtpLoading by viewModel.isOtpLoading.collectAsStateWithLifecycle()
    val isOtpError by viewModel.isOtpError.collectAsStateWithLifecycle()
    val otpResendState by viewModel.otpResendState.collectAsStateWithLifecycle()

    ScreenContent(
        phone = phone,
        otp = otp,
        onOtpChanged = viewModel::onOtpChanged,
        onOtpEntered = viewModel::onOtpEntered,
        isOtpLoading = isOtpLoading,
        isOtpError = isOtpError,
        otpResendState = otpResendState,
        onResendOtpClicked = viewModel::onResendOtpClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    phone: PhoneNumber,
    otp: String,
    onOtpChanged: (String) -> Unit,
    onOtpEntered: () -> Unit,
    isOtpLoading: Boolean,
    isOtpError: Boolean,
    otpResendState: OtpResendState,
    onResendOtpClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (SignUpOtpScreenAction) -> Unit,
) {
    SignUpOtpScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    val otpFocusRequester = remember { FocusRequester() }

    LifecycleStartEffect(otpFocusRequester) {
        otpFocusRequester.tryRequestFocus()
        onStopOrDispose {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.systemBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            ),
    ) {
        TopBar(onBackClicked = onBackClicked)

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            SmsOtp(
                phone = phone,
                otp = otp,
                onOtpChanged = onOtpChanged,
                onOtpEntered = { onOtpEntered() },
                isOtpError = isOtpError,
                isOtpLoading = isOtpLoading,
                resendState = otpResendState,
                onResendClicked = onResendOtpClicked,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.focusRequester(otpFocusRequester),
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
