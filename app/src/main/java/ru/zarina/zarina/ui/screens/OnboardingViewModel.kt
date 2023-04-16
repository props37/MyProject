package ru.zarina.zarina.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.exception.MissingPermissionException
import ru.zarina.zarina.domain.exception.ServiceUnavailableException
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
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
                        .onFailure { throwable ->
                            val message = when (throwable) {
                                is MissingPermissionException -> Text.Resource(R.string.cant_detect_city_without_permission)
                                is ServiceUnavailableException -> Text.Resource(R.string.location_services_unavailable)
                                else -> Text.Resource(R.string.cant_detect_city)
                            }
                            sideEffect(SideEffect.ShowError(message))
                        }
                }
            }
        } else {
            sideEffect(SideEffect.ShowError(Text.Resource(R.string.cant_detect_city_without_permission)))
        }
    }

    fun onSelectClick() {}

    fun onCloseClick() {
        sideEffect(SideEffect.ShowHome)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object ShowHome : SideEffect
        object RequestLocationPermission : SideEffect
        class ShowError(val message: Text) : SideEffect
    }

    enum class Operation : OperationKey { DETECT_CITY }

}
