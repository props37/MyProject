package ru.livetyping.zarina.ui.screen.signin

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.ui.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.screen.signin.SignInViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SignInInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val currentSignInTypeValueHolder = savedStateHandle.createValueHolder(
        key = KEY_CURRENT_SIGN_IN_TYPE,
        initialValue = SignInType.EMAIL,
    )

    private val emailValueHolder = savedStateHandle.createValueHolder(
        key = KEY_EMAIL,
        initialValue = "",
    )

    private val passwordValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PASSWORD,
        initialValue = "",
    )

    private val phoneValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PHONE,
        initialValue = PHONE_NUMBER_INITIAL_VALUE,
    )

    val signInTypes: StateFlow<ImmutableList<SignInType>> =
        MutableStateFlow(SignInType.entries.toImmutableList()).asStateFlow()

    val currentSignInType: StateFlow<SignInType> = currentSignInTypeValueHolder.stateFlow

    val email: StateFlow<String> = emailValueHolder.stateFlow

    val password: StateFlow<String> = passwordValueHolder.stateFlow

    val phone: StateFlow<String> = phoneValueHolder.stateFlow

    fun onBackClicked() {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onSignInTypeChanged(type: SignInType) {
        currentSignInTypeValueHolder.set(type)
    }

    fun onEmailChanged(email: String) {
        emailValueHolder.set(email)
    }

    fun onPasswordChanged(password: String) {
        passwordValueHolder.set(password)
    }

    fun onPhoneChanged(phone: String) {
        phoneValueHolder.set(phone)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect

    @Parcelize
    enum class SignInType : Parcelable { EMAIL, PHONE }

    companion object {
        private const val KEY_CURRENT_SIGN_IN_TYPE = "current_sign_in_type"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PHONE = "phone"

        private const val PHONE_NUMBER_INITIAL_VALUE = "+7"
    }
}
