package ru.livetyping.zarina.feature.signup.ui.impl.impl.signup

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.signup.ui.impl.impl.signup.model.SignUpState
import javax.inject.Inject

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SignUpSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    @OptIn(SavedStateHandleSaveableApi::class)
    private val nameTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isNameInvalid = MutableStateFlow(false)

    private val birthDateEpochMillisValueHolder = savedStateHandle.createValueHolder<Long?>(
        key = Keys.BIRTH_DATE.key,
        initialValue = null,
    )

    private val isBirthDateInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val emailTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isEmailInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val phoneTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState(PHONE_INITIAL_TEXT) },
    )

    private val isPhoneInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val passwordTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isPasswordInvalid = MutableStateFlow(false)

    private val receiveEmailsValueHolder = savedStateHandle.createValueHolder(
        key = Keys.RECEIVE_EMAILS.key,
        initialValue = false,
    )

    private val receiveSmsValueHolder = savedStateHandle.createValueHolder(
        key = Keys.RECEIVE_SMS.key,
        initialValue = false,
    )

    val signUpState: StateFlow<SignUpState> = combineMore(
        isNameInvalid,
        birthDateEpochMillisValueHolder.stateFlow,
        isBirthDateInvalid,
        isEmailInvalid,
        isPhoneInvalid,
        isPasswordInvalid,
        receiveEmailsValueHolder.stateFlow,
        receiveSmsValueHolder.stateFlow,
    ) { isNameInvalid, birthDateEpochMillis, isBirthDateInvalid, isEmailInvalid, isPhoneInvalid, isPasswordInvalid, receiveEmails, receiveSms ->
        SignUpState(
            nameTextFieldState = nameTextFieldState,
            isNameInvalid = isNameInvalid,
            birthDateEpochMillis = birthDateEpochMillis,
            isBirthDateInvalid = isBirthDateInvalid,
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid,
            receiveEmails = receiveEmails,
            receiveSms = receiveSms,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SignUpState(
            nameTextFieldState = nameTextFieldState,
            isNameInvalid = isNameInvalid.value,
            birthDateEpochMillis = birthDateEpochMillisValueHolder.get(),
            isBirthDateInvalid = isBirthDateInvalid.value,
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid.value,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid.value,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid.value,
            receiveEmails = receiveEmailsValueHolder.get(),
            receiveSms = receiveSmsValueHolder.get(),
        ),
    )

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignUpScreenAction.ScreenClosed
            emitSideEffect(SignUpSideEffect.Navigate(action))
        }
    }

    private enum class Keys {
        BIRTH_DATE,
        RECEIVE_EMAILS,
        RECEIVE_SMS;

        val key: String get() = name
    }

    private companion object {
        private const val PHONE_INITIAL_TEXT = "+7"
    }
}
