package ru.livetyping.zarina.ui.common.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaOtpTextField
import ru.livetyping.zarina.ui.common.otp.OtpResendState
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.rememberFormattedPhoneNumber
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun SmsOtp(
    phone: PhoneNumber,
    otp: String,
    onOtpChanged: (String) -> Unit,
    onOtpEntered: (String) -> Unit,
    resendState: OtpResendState,
    onResendClicked: () -> Unit,
    modifier: Modifier = Modifier,
    otpLength: Int = Length,
    isOtpEnabled: Boolean = true,
    isOtpLoading: Boolean = false,
    isOtpInvalid: Boolean = false,
    isOtpReadOnly: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .padding(contentPadding),
    ) {
        Text(
            text = stringResource(R.string.enter_sms_code_from_message),
            style = UiKitTheme.typography.secondary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
        )
        Spacer(modifier = Modifier.height(8.dp))
        val formattedPhone = rememberFormattedPhoneNumber(phone)
        Text(
            text = stringResource(R.string.we_sent_sms_code_to_phone_number, formattedPhone),
            style = UiKitTheme.typography.tertiary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(24.dp))

        ZarinaOtpTextField(
            value = otp,
            onValueChanged = onOtpChanged,
            onFilled = onOtpEntered,
            length = otpLength,
            isEnabled = isOtpEnabled,
            isLoading = isOtpLoading,
            isError = isOtpInvalid,
            isReadOnly = isOtpReadOnly,
            backgroundColor = backgroundColor,
            keyboardActions = keyboardActions,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            targetState = resendState,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.BottomCenter,
            contentKey = {
                when (it) {
                    OtpResendState.ResendAvailable -> it
                    is OtpResendState.TimeoutCountdown -> ContentKeyResendTimeoutCountdown
                }
            },
            label = "Resend content",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) { resendState ->
            when (resendState) {
                OtpResendState.ResendAvailable -> {
                    ZarinaButton(
                        onClick = onResendClicked,
                        size = ZarinaButtonSize.Medium,
                        colors = ZarinaButtonDefaults.backlessColors(),
                    ) {
                        Text(text = stringResource(R.string.resend).uppercase())
                    }
                }

                is OtpResendState.TimeoutCountdown -> {
                    val remainingTime = resendState.remainingTime.toComponents { minutes, seconds, _ ->
                        RemainingTimeFormat.format(minutes, seconds)
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.heightIn(min = ZarinaButtonDefaults.SizeMedium),
                    ) {
                        Text(
                            text = stringResource(R.string.code_can_be_sent_again_after, remainingTime),
                            style = UiKitTheme.typography.tertiary.regular,
                            color = UiKitTheme.colors.text.general.regular.default,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewResendTimeout() {
    ZarinaPreview {
        var remainingTime by remember { mutableStateOf(1.minutes) }
        var resendState by remember {
            mutableStateOf<OtpResendState>(OtpResendState.TimeoutCountdown(remainingTime))
        }
        LaunchedEffect(Unit) {
            while (resendState != OtpResendState.ResendAvailable) {
                delay(100.milliseconds)
                remainingTime -= 1.seconds

                resendState = if (remainingTime > Duration.ZERO) {
                    OtpResendState.TimeoutCountdown(remainingTime)
                } else {
                    OtpResendState.ResendAvailable
                }
            }
        }

        SmsOtp(
            phone = remember { PhoneNumber.create("+78005553535") },
            otp = "",
            onOtpChanged = {},
            onOtpEntered = {},
            resendState = resendState,
            onResendClicked = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewResendAvailable() {
    ZarinaPreview {
        SmsOtp(
            phone = remember { PhoneNumber.create("+78005553535") },
            otp = "12",
            onOtpChanged = {},
            onOtpEntered = {},
            resendState = remember { OtpResendState.ResendAvailable },
            onResendClicked = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

private const val Length = 4

private const val ContentKeyResendTimeoutCountdown = "ContentKeyResendTimeoutCountdown"

private const val RemainingTimeFormat = "%d:%02d"
