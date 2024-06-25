package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber

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
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.ChangePhoneNumberViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class ChangePhoneNumberViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ChangePhoneNumberInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var changePhoneJob: Job? = null

    private val phoneValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PHONE,
        initialValue = PHONE_INITIAL_VALUE
    )

    val phone: StateFlow<String> = phoneValueHolder.stateFlow

    private val _isPhoneInvalid = MutableStateFlow(false)
    val isPhoneInvalid = _isPhoneInvalid.asStateFlow()

    val isChangePhoneButtonLoading = operationTracker
        .isOperationOngoing(Operation.CHANGE_PHONE)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ChangePhoneNumberScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onPhoneChanged(phone: String) {
        phoneValueHolder.set(phone)
    }

    fun onPhoneEntered() {
        onChangePhoneClicked()
    }

    fun onChangePhoneClicked() {
        if (changePhoneJob?.isActive == true) return

        changePhoneJob = viewModelScope.launch {
            operationTracker.track(Operation.CHANGE_PHONE) {
                // TODO: [High] Implement
            }
        }
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ChangePhoneNumberScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect
    }

    private enum class Operation : OperationKey { CHANGE_PHONE }

    companion object {
        private const val PHONE_INITIAL_VALUE = "+7"

        private const val KEY_PHONE = "phone"
    }
}
