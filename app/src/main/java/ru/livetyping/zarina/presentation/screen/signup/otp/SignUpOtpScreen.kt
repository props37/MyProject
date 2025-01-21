package ru.livetyping.zarina.presentation.screen.signup.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.otp.OtpResendState
import ru.livetyping.zarina.presentation.common.yandexcaptcha.YandexCaptchaDialog
import ru.livetyping.zarina.presentation.common.yandexcaptcha.YandexCaptchaDialogState
import ru.livetyping.zarina.presentation.screen.common.otp.SmsOtpScreenContent
import ru.livetyping.zarina.presentation.screen.signup.otp.SignUpOtpViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun SignUpOtpScreen(
    navigate: (SignUpOtpScreenAction) -> Unit,
    viewModel: SignUpOtpViewModel = hiltViewModel(),
) {
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val otp by viewModel.otp.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate,
    )
    val isOtpLoading by viewModel.isOtpLoading.collectAsStateWithLifecycle()
    val isOtpInvalid by viewModel.isOtpInvalid.collectAsStateWithLifecycle()
    val otpResendState by viewModel.otpResendState.collectAsStateWithLifecycle()
    val yandexCaptchaDialogState by viewModel.yandexCaptchaState.collectAsStateWithLifecycle()
    val isResendButtonLoading by viewModel.isResendButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        phone = phone,
        otp = otp,
        onOtpChanged = viewModel::onOtpChanged,
        onOtpEntered = viewModel::onOtpEntered,
        isOtpLoading = isOtpLoading,
        isOtpInvalid = isOtpInvalid,
        otpResendState = otpResendState,
        onResendOtpClicked = viewModel::onResendOtpClicked,
        isResendButtonLoading = isResendButtonLoading,
        onBackClicked = viewModel::onBackClicked,
        yandexCaptchaDialogState = yandexCaptchaDialogState,
        onYandexCaptchaDismissRequested = viewModel::onYandexCaptchaDismissRequested,
        onYandexCaptchaTokenReceived = viewModel::onYandexCaptchaTokenReceived,
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
    isOtpInvalid: Boolean,
    otpResendState: OtpResendState,
    isResendButtonLoading: Boolean,
    onResendOtpClicked: () -> Unit,
    onBackClicked: () -> Unit,
    yandexCaptchaDialogState: YandexCaptchaDialogState,
    onYandexCaptchaDismissRequested: () -> Unit,
    onYandexCaptchaTokenReceived: (YandexCaptchaToken) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (SignUpOtpScreenAction) -> Unit,
) {
    SignUpOtpScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Box {
        SmsOtpScreenContent(
            onBackClicked = onBackClicked,
            topBarTitle = stringResource(R.string.registration),
            phone = phone,
            otp = otp,
            onOtpChanged = onOtpChanged,
            onOtpEntered = { onOtpEntered() },
            onImeDoneClicked = onOtpEntered,
            isOtpInvalid = isOtpInvalid,
            isOtpLoading = isOtpLoading,
            otpResendState = otpResendState,
            onResendOtpClicked = onResendOtpClicked,
            isResendButtonLoading = isResendButtonLoading,
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.background.general.regular.default)
                .windowInsetsPadding(
                    WindowInsets.systemBars
                        .union(WindowInsets.displayCutout)
                        .union(WindowInsets.ime),
                )
                .bottomNavBarPadding(WindowInsets.ime),
        )

        YandexCaptchaDialog(
            state = yandexCaptchaDialogState,
            onDismissRequest = onYandexCaptchaDismissRequested,
            onTokenReceived = onYandexCaptchaTokenReceived,
        )
    }
}
