package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.common.otp.OtpResendState
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.common.otp.SmsOtpScreenContent
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.otp.ChangePhoneNumberOtpViewModel.SideEffect

@Composable
fun ChangePhoneNumberOtpScreen(
    navigate: (ChangePhoneNumberOtpScreenAction) -> Unit,
    viewModel: ChangePhoneNumberOtpViewModel = hiltViewModel(),
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
    navigate: (ChangePhoneNumberOtpScreenAction) -> Unit,
) {
    ChangePhoneNumberOtpScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    SmsOtpScreenContent(
        onBackClicked = onBackClicked,
        topBarTitle = stringResource(R.string.phone_change),
        phone = phone,
        otp = otp,
        onOtpChanged = onOtpChanged,
        onOtpEntered = { onOtpEntered() },
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
        // TODO: [Low] Add preview
    }
}
