package ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class RecipientState(
    val firstNameTextFieldState: TextFieldState,
    val isFirstNameInvalid: Boolean,
    val lastNameTextFieldState: TextFieldState,
    val isLastNameInvalid: Boolean,
    val phoneTextFieldState: TextFieldState,
    val isPhoneInvalid: Boolean,
    val emailTextFieldState: TextFieldState,
    val isEmailInvalid: Boolean,
)
