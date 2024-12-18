package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.otp.NewOtpRequestState
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
internal class PhoneConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<PhoneConfirmationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var confirmPhoneJob: Job? = null
    private var requestNewOtpJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<PhoneConfirmationNavEntry>()

    @OptIn(SavedStateHandleSaveableApi::class)
    private val otpTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isOtpInvalid = MutableStateFlow(false)

    private val newOtpRequestState = MutableStateFlow<NewOtpRequestState>(
        NewOtpRequestState.Unavailable(NEW_OTP_REQUEST_TIMEOUT),
    )

    val phone: StateFlow<PhoneNumber> = ReadOnlyStateFlow(PhoneNumber.create(navEntry.phone))

    val otpState: StateFlow<TextFieldOtpState> = combine(
        isOtpInvalid,
        newOtpRequestState,
        operationTracker.ongoingOperationKeys,
    ) { isOtpInvalid, newOtpRequestState, ongoingOperations ->
        TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = Operation.CONFIRM_PHONE in ongoingOperations,
            isInvalid = isOtpInvalid,
            newOtpRequestState = newOtpRequestState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = false,
            isInvalid = false,
            newOtpRequestState = NewOtpRequestState.Available,
        ),
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneConfirmationScreenAction.BackClicked
            emitSideEffect(PhoneConfirmationSideEffect.Navigate(action))
        }
    }

    private enum class Operation : OperationKey { CONFIRM_PHONE }

    private companion object {
        private val NEW_OTP_REQUEST_TIMEOUT get() = 1.minutes
    }
}
