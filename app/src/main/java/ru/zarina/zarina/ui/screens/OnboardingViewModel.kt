package ru.zarina.zarina.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val interactor: OnboardingInteractor,
) : ViewModel(),
    ISideEffectSource<OnboardingViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()
    val isDetectButtonLoading = operationTracker
        .isOperationOngoing(Operation.DETECT_CITY)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    private val _detectedCity = MutableStateFlow<City?>(null)

    fun onDetectClick() {
        sideEffect(SideEffect.RequestLocationPermission)
    }

    fun onLocationPermissionResult(isGranted: Boolean) {
        Timber.v("Location permission is granted: $isGranted")
        if (isGranted) {
            viewModelScope.launch {
                operationTracker.track(Operation.DETECT_CITY) {
                    interactor.detectCity()
                        .onSuccess { _detectedCity.value = it }
                        .onFailure {
                            // TODO display error
                        }
                }
            }
        } else {
            // TODO not granted, display error
        }
    }

    fun onSelectClick() {}

    fun onCloseClick() {
        sideEffect(SideEffect.ShowHome)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object ShowHome : SideEffect
        object RequestLocationPermission : SideEffect
    }

    enum class Operation : OperationKey { DETECT_CITY }

}
