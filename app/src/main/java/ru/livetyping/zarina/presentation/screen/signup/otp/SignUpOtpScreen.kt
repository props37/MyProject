package ru.livetyping.zarina.presentation.screen.signup.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.common.otp.OtpResendState
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.common.otp.SmsOtpScreenContent
import ru.livetyping.zarina.presentation.screen.signup.otp.SignUpOtpViewModel.SideEffect
import kotlin.time.Duration.Companion.minutes

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

    ScreenContent(
        phone = phone,
        otp = otp,
        onOtpChanged = viewModel::onOtpChanged,
        onOtpEntered = viewModel::onOtpEntered,
        isOtpLoading = isOtpLoading,
        isOtpInvalid = isOtpInvalid,
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
    isOtpInvalid: Boolean,
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
    )
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            phone = remember { PhoneNumber.create("+78005553535") },
            otp = "",
            onOtpChanged = {},
            onOtpEntered = {},
            isOtpLoading = false,
            isOtpInvalid = false,
            otpResendState = remember { OtpResendState.TimeoutCountdown(1.minutes) },
            onResendOtpClicked = {},
            onBackClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
