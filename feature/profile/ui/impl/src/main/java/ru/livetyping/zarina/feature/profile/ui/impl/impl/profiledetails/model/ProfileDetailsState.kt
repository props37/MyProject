package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class ProfileDetailsState {
    @Stable
    data class Success(
        val firstNameTextFieldState: TextFieldState,
        val lastNameTextFieldState: TextFieldState,
        val birthDateEpochMillis: Long?,
        val phoneTextFieldState: TextFieldState,
        val emailTextFieldState: TextFieldState,
        val receiveEmails: Boolean,
        val receiveSms: Boolean,
    ) : ProfileDetailsState()

    @Immutable
    data object Loading : ProfileDetailsState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : ProfileDetailsState()
}
