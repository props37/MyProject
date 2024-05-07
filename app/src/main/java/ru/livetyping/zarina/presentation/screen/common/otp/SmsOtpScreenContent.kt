package ru.livetyping.zarina.presentation.screen.common.otp

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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.presentation.common.component.SmsOtp
import ru.livetyping.zarina.presentation.common.otp.OtpResendState
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.common.otp.OtpScreenComponents.TopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.tryRequestFocus
import kotlin.time.Duration.Companion.minutes

@Composable
fun SmsOtpScreenContent(
    onBackClicked: () -> Unit,
    topBarTitle: String,
    phone: PhoneNumber,
    otp: String,
    onOtpChanged: (String) -> Unit,
    onOtpEntered: (String) -> Unit,
    isOtpInvalid: Boolean,
    isOtpLoading: Boolean,
    otpResendState: OtpResendState,
    onResendOtpClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val otpFocusRequester = remember { FocusRequester() }

    LifecycleStartEffect(otpFocusRequester) {
        otpFocusRequester.tryRequestFocus()
        onStopOrDispose {}
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.systemBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            ),
    ) {
        TopBar(
            title = topBarTitle,
            onBackClicked = onBackClicked,
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(24.dp))

            SmsOtp(
                phone = phone,
                otp = otp,
                onOtpChanged = onOtpChanged,
                onOtpEntered = onOtpEntered,
                isOtpInvalid = isOtpInvalid,
                isOtpLoading = isOtpLoading,
                resendState = otpResendState,
                onResendClicked = onResendOtpClicked,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(otpFocusRequester),
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        SmsOtpScreenContent(
            onBackClicked = {},
            topBarTitle = "Регистрация",
            phone = remember { PhoneNumber.create("+78005553535") },
            otp = "",
            onOtpChanged = {},
            onOtpEntered = {},
            isOtpInvalid = false,
            isOtpLoading = false,
            otpResendState = remember { OtpResendState.TimeoutCountdown(1.minutes) },
            onResendOtpClicked = {},
        )
    }
}
