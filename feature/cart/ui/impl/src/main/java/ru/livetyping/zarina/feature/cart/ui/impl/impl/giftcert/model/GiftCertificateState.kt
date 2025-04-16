package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class GiftCertificateState(
    val giftCertificateNumberTextFieldState: TextFieldState,
    val giftCertificateVerificationCodeTextFieldState: TextFieldState,
    val isGiftCertificateNumberInvalid: Boolean,
    val isGiftCertificateVerificationCodeInvalid: Boolean,
    val isApplyButtonLoading: Boolean,
)
