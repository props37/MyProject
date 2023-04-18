package ru.zarina.zarina.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Url
import ru.zarina.zarina.domain.exception.MissingPermissionException
import ru.zarina.zarina.domain.exception.ServiceUnavailableException
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.MessageQueue
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.utils.isNetworkException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val interactor: OnboardingInteractor,
) : ViewModel(),
    ISideEffectSource<OnboardingViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()
    private val messageQueue = MessageQueue(viewModelScope)

    private val _step = MutableStateFlow(OnboardingStep.CITY_SELECTION_TYPE)
    val step = _step.asStateFlow()
    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState = _splashState.asStateFlow()
    val isDetectButtonLoading = operationTracker
        .isOperationOngoing(Operation.DETECT_CITY)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
    private val _detectedCity = MutableStateFlow<City?>(null)
    val detectedCity = _detectedCity.asStateFlow()
    val isConfirmDetectedCityButtonLoading = operationTracker
        .isOperationOngoing(Operation.FINISH_ONBOARDING)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isSnackbarVisible = messageQueue.isMessageVisible
    val snackbarText = messageQueue.message

    init {
        fetchRemoteSplash()
    }

    private fun fetchRemoteSplash() {
        viewModelScope.launch {
            interactor.getOnboardingSplash()
                .onSuccess { _splashState.value = SplashState.Success(it) }
                .onFailure { _splashState.value = SplashState.Error }
        }
    }

    fun onDetectClick() {
        sideEffect(SideEffect.RequestLocationPermission)
    }

    fun onLocationPermissionResult(isGranted: Boolean) {
        Timber.v("Location permission is granted: $isGranted")
        if (isGranted) {
            viewModelScope.launch {
                operationTracker.track(Operation.DETECT_CITY) {
                    interactor.detectCity()
                        .onSuccess {
                            _detectedCity.value = it
                            _step.value = OnboardingStep.DETECTION_RESULT
                        }
                        .onFailure { throwable ->
                            val message = when {
                                throwable.isNetworkException() -> Text.Resource(R.string.network_error)
                                throwable is MissingPermissionException -> Text.Resource(R.string.cant_detect_city_without_permission)
                                throwable is ServiceUnavailableException -> Text.Resource(R.string.location_services_unavailable)
                                else -> Text.Resource(R.string.cant_detect_city)
                            }
                            messageQueue.showMessage(message)
                        }
                }
            }
        } else {
            messageQueue.showMessage(Text.Resource(R.string.cant_detect_city_without_permission))
        }
    }

    fun onSelectManuallyClick() {
        // TODO cancel city detection job
        // TODO
    }

    fun onConfirmDetectedCityClick() {
        viewModelScope.launch {
            operationTracker.track(Operation.FINISH_ONBOARDING) {
                interactor.finishOnboarding(detectedCity.value)
                    .onSuccess { sideEffect(SideEffect.ShowHome) }
                    .onFailure { throwable ->
                        val message = when {
                            throwable.isNetworkException() -> Text.Resource(R.string.network_error)
                            else -> Text.Resource(R.string.cant_save_selected_city)
                        }
                        messageQueue.showMessage(message)
                    }
            }
        }
    }

    fun onCloseClick() {
        viewModelScope.launch {
            operationTracker.track(Operation.FINISH_ONBOARDING) {
                interactor.finishOnboarding(null)
                    .onSuccess { sideEffect(SideEffect.ShowHome) }
            }
        }
    }

    sealed class SplashState {
        object Loading : SplashState()
        data class Success(val url: Url) : SplashState()
        object Error : SplashState()
    }

    enum class OnboardingStep { CITY_SELECTION_TYPE, DETECTION_RESULT }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object ShowHome : SideEffect
        object RequestLocationPermission : SideEffect
    }

    enum class Operation : OperationKey { DETECT_CITY, FINISH_ONBOARDING }

}
