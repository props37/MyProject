package ru.livetyping.zarina.core.uikit.otp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.uicommon.otp.NewOtpRequestState
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState
import ru.livetyping.zarina.core.uicompose.rememberFormattedPhoneNumber
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.otp.SmsOtpDefaults.RemainingTimeFormat
import ru.livetyping.zarina.core.uikit.text.ZarinaOtpTextField
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun SmsOtp(
    otpState: TextFieldOtpState,
    phone: PhoneNumber,
    onOtpEntered: (String) -> Unit,
    onRequestNewOtpClicked: () -> Unit,
    modifier: Modifier = Modifier,
    otpLength: Int = 4,
    onKeyboardAction: KeyboardActionHandler? = null,
    backgroundColor: Color = SmsOtpDefaults.BackgroundColor,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .padding(contentPadding),
    ) {
        Text(
            text = stringResource(R.string.uikit_enter_code_from_sms),
            style = UiKitTheme.typography.secondary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
        )
        Spacer(modifier = Modifier.height(8.dp))
        val formattedPhone = rememberFormattedPhoneNumber(phone.value)
        val phoneString = formattedPhone ?: phone.value
        Text(
            text = stringResource(R.string.uikit_we_sent_sms_code_to_phone_number, phoneString),
            style = UiKitTheme.typography.tertiary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(24.dp))

        ZarinaOtpTextField(
            state = otpState.textFieldState,
            onFilled = onOtpEntered,
            length = otpLength,
            isLoading = otpState.isLoading,
            isError = otpState.isInvalid,
            backgroundColor = backgroundColor,
            onKeyboardAction = onKeyboardAction,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            targetState = otpState.newOtpRequestState,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.BottomCenter,
            contentKey = {
                when (it) {
                    NewOtpRequestState.Available -> it
                    is NewOtpRequestState.Unavailable -> NewOtpRequestContentKey.Unavailable
                }
            },
            label = "new OTP request",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) { newOtpRequestState ->
            when (newOtpRequestState) {
                NewOtpRequestState.Available -> {
                    ZarinaButton(
                        onClick = onRequestNewOtpClicked,
                        isLoading = otpState.isLoading,
                        size = ZarinaButtonSize.Medium,
                        colors = ZarinaButtonDefaults.backlessColors(),
                    ) {
                        Text(text = stringResource(R.string.uikit_resend_code).uppercase())
                    }
                }

                is NewOtpRequestState.Unavailable -> {
                    val remainingTime = newOtpRequestState.timeout.toComponents { minutes, seconds, _ ->
                        RemainingTimeFormat.format(minutes, seconds)
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.heightIn(min = ZarinaButtonDefaults.SizeMedium),
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.uikit_code_can_be_sent_again_after,
                                remainingTime,
                            ),
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

public object SmsOtpDefaults {
    internal val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    internal const val RemainingTimeFormat = "%d:%02d"
}

private enum class NewOtpRequestContentKey { Unavailable }
