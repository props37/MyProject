package ru.zarina.zarina.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val interactor: OnboardingInteractor,
) : ViewModel(),
    ISideEffectSource<OnboardingViewModel.SideEffect> by SideEffectQueue() {

    private val _detectedCity = MutableStateFlow<City?>(null)

    fun onDetectClick() {
        sideEffect(SideEffect.RequestLocationPermission)
    }

    fun onLocationPermissionResult(isGranted: Boolean) {
        Timber.v("Location permission is granted: $isGranted")
        if (isGranted) {
            viewModelScope.launch {
                interactor.detectCity()
                    .onSuccess { _detectedCity.value = it }
                    .onFailure {
                        // TODO display error
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

}
