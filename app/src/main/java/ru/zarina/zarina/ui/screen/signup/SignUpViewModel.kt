package ru.zarina.zarina.ui.screen.signup

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.screen.signup.SignUpViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SignUpInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

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

    fun onContinueClicked() {
        // TODO: [High] Implement
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignUpScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect
    }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_RECEIVE_NEWS_BE_EMAIL = "receive_new_by_email"
        private const val KEY_RECEIVE_SMS_NOTIFICATIONS = "receive_sms_notifications"
        private const val KEY_ARE_POLICIES_ACCEPTED = "are_policies_accepted"
    }
}
