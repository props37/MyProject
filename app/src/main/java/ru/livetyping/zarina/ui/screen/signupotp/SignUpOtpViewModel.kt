package ru.livetyping.zarina.ui.screen.signupotp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.ui.screen.signupotp.SignUpOtpViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.ConfirmSignUpUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class SignUpOtpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SignUpOtpInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var confirmSignUpJob: Job? = null

    val phone: StateFlow<PhoneNumber> = savedStateHandle
        .getStateFlow<String?>(
            key = SignUpGraph.Otp.ARG_KEY_PHONE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { string ->
            checkNotNull(string) { "phone is null" }
            PhoneNumber.create(string)
        }

    val otp: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_OTP,
        initialValue = "",
    )

    val isOtpLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.CONFIRM_SIGN_UP)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    private val _isOtpError = MutableStateFlow(false)
    val isOtpError: StateFlow<Boolean> = _isOtpError.asStateFlow()

    fun onBackClicked() {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onOtpChanged(otp: String) {
        savedStateHandle[KEY_OTP] = otp
    }

    fun onOtpEntered() {
        if (confirmSignUpJob?.isActive == true) return

        confirmSignUpJob = viewModelScope.launch {
            operationTracker.track(Operation.CONFIRM_SIGN_UP) {
                val params = ConfirmSignUpUseCase.Params(phone.value, otp.value)
                interactor.confirmSignUp(params)
                    .onSuccess {
                        // TODO: [High] Implement
                    }
                    .onFailure {
                        // TODO: [High] Implement
                    }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect

    private enum class Operation : OperationKey { CONFIRM_SIGN_UP }

    companion object {
        private const val KEY_OTP = "otp"
    }
}
