package ru.zarina.zarina.ui.screen.signup

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.base.operationtracker.OperationKey
import ru.zarina.zarina.base.operationtracker.OperationTracker
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaToastMessageStyle
import ru.zarina.zarina.ui.screen.signup.SignUpViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SignUpInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var signUpJob: Job? = null

    val name: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_NAME,
        initialValue = "",
    )

    val email: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_EMAIL,
        initialValue = "",
    )

    val phone: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_PHONE,
        initialValue = "",
    )

    val password: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_PASSWORD,
        initialValue = "",
    )

    val receiveNewsByEmail: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_RECEIVE_NEWS_BE_EMAIL,
        initialValue = false,
    )

    val receiveSmsNotifications: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_RECEIVE_SMS_NOTIFICATIONS,
        initialValue = false,
    )

    val arePoliciesAccepted: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_ARE_POLICIES_ACCEPTED,
        initialValue = false,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignUpScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onNameChanged(name: String) {
        savedStateHandle[KEY_NAME] = name
    }

    fun onEmailChanged(email: String) {
        savedStateHandle[KEY_EMAIL] = email
    }

    fun onPhoneChanged(phone: String) {
        val normalizedPhone = PhoneNumberUtils.normalizeNumber(phone)
        savedStateHandle[KEY_PHONE] = normalizedPhone
    }

    fun onPasswordChanged(password: String) {
        savedStateHandle[KEY_PASSWORD] = password
    }

    fun onReceiveNewsNyEmailChanged(value: Boolean) {
        savedStateHandle[KEY_RECEIVE_NEWS_BE_EMAIL] = value
    }

    fun onReceiveSmsNotificationsChanged(value: Boolean) {
        savedStateHandle[KEY_RECEIVE_SMS_NOTIFICATIONS] = value
    }

    fun onPoliciesAcceptedChanged(areAccepted: Boolean) {
        savedStateHandle[KEY_ARE_POLICIES_ACCEPTED] = areAccepted
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    fun onSignUpClicked() {
        if (signUpJob?.isActive == true) return

        if (!arePoliciesAccepted.value) {
            savedStateHandle[KEY_IS_POLICIES_ERROR_VISIBLE] = true
            val messageText = Text.Resource(R.string.sign_up_agreement_error)
            val message = ZarinaToastMessage(
                text = messageText,
                style = ZarinaToastMessageStyle.ERROR,
            )
            emitSideEffect(SideEffect.ShowZarinaToast(message))
            return
        }

        signUpJob = viewModelScope.launch {
            operationTracker.track(Operation.SIGN_UP) {
                // TODO: [High] Implement
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignUpScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { SIGN_UP }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_RECEIVE_NEWS_BE_EMAIL = "receive_new_by_email"
        private const val KEY_RECEIVE_SMS_NOTIFICATIONS = "receive_sms_notifications"
        private const val KEY_ARE_POLICIES_ACCEPTED = "are_policies_accepted"
        private const val KEY_IS_POLICIES_ERROR_VISIBLE = "is_policies_error_visible"
    }
}
