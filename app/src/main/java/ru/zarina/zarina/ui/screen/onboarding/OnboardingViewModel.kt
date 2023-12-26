package ru.zarina.zarina.ui.screen.onboarding

import android.Manifest
import android.os.Build
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.permissionmanager.isDenied
import ru.zarina.zarina.data.permissionmanager.isGranted
import ru.zarina.zarina.data.permissionmanager.shouldShowRequestRationale
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.device.SetIsOnboardingCompletedUseCase
import ru.zarina.zarina.util.library.coroutines.mapState
import ru.zarina.zarina.utils.clean.invoke
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: OnboardingInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val permissionManager = interactor.permissionManager

    private var detectCityJob: Job? = null

    val onboardingSteps: StateFlow<List<OnboardingStep>> = savedStateHandle.getStateFlow(
        key = KEY_ONBOARDING_STEPS,
        initialValue = createOnboardingSteps(),
    )

    val currentOnboardingStep: StateFlow<OnboardingStep> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_ONBOARDING_STEP,
        initialValue = onboardingSteps.value.firstOrNull() ?: OnboardingStep.CITY_DETECTION,
    )

    val currentCity: StateFlow<City?> = savedStateHandle
        .getStateFlow<CityParcelable?>(
            key = KEY_CURRENT_CITY,
            initialValue = null,
        )
        .mapState(viewModelScope) { cityParcelable ->
            cityParcelable?.toCity()
        }

    val isDetectCityButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.DETECT_CITY)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    fun onRequestNotificationsPermissionClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            viewModelScope.launch {
                val currentPermissionState = permissionManager.getPermissionState(permission)
                if (currentPermissionState.isGranted) {
                    showOnboardingStep(OnboardingStep.CITY_DETECTION)
                } else {
                    val newPermissionState = permissionManager.requestPermission(permission)
                    if (newPermissionState != currentPermissionState) {
                        // User has either granted or denied the permission
                        showOnboardingStep(OnboardingStep.CITY_DETECTION)
                    } else if (
                        newPermissionState.isDenied && !newPermissionState.shouldShowRequestRationale
                    ) {
                        val hasPermissionRequiredRequestRationale =
                            permissionManager.hasPermissionRequiredRequestRationale(permission)
                                .firstOrNull() ?: false
                        if (hasPermissionRequiredRequestRationale) {
                            // User has denied the permission permanently
                            showOnboardingStep(OnboardingStep.CITY_DETECTION)
                        }
                    }
                }
            }
        } else {
            showOnboardingStep(OnboardingStep.CITY_DETECTION)
        }
    }

    fun onDetectCityClicked() {
        if (detectCityJob?.isActive == true) return

        val permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        detectCityJob = viewModelScope.launch {
            val currentPermissionsState = permissionManager.getMultiplePermissionsState(permissions)
            if (currentPermissionsState.any { it.value.isGranted }) {
                detectCity()
            } else {
                val newPermissionsState = permissionManager.requestMultiplePermissions(permissions)
                if (newPermissionsState != currentPermissionsState) {
                    // User has either granted or denied the permission
                    if (newPermissionsState.any { it.value.isGranted }) {
                        detectCity()
                    } else {
                        closeOnboarding()
                    }
                } else if (
                    // TODO: [High] Ensure this works correctly
                    newPermissionsState.all { it.value.isDenied }
                    && newPermissionsState.any { !it.value.shouldShowRequestRationale }
                ) {
                    val havePermissionsRequiredRequestRationale =
                        permissionManager.haveMultiplePermissionsRequiredRequestRationale(permissions)
                            .firstOrNull() ?: emptyMap()
                    if (havePermissionsRequiredRequestRationale.any { it.value == true }) {
                        // User has denied the permission permanently
                        savedStateHandle[KEY_CURRENT_CITY] =
                            CityParcelable.fromCity(City.SAINT_PETERSBURG)
                        showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
                    }
                }
            }
        }
    }

    fun onSkipCityDetectionClicked() {
        closeOnboarding()
    }

    fun onConfirmCityClicked() {
        closeOnboarding()
    }

    fun onSelectCityClicked() {
        navigationThrottler.throttle {
            val action = OnboardingScreenAction.SelectCityClicked(currentCity.value)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    private suspend fun detectCity() {
        operationTracker.track(Operation.DETECT_CITY) {
            interactor.detectCurrentCity()
                .onSuccess { city ->
                    savedStateHandle[KEY_CURRENT_CITY] = city?.let { CityParcelable.fromCity(it) }
                    showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
                }
                .onFailure {
                    savedStateHandle[KEY_CURRENT_CITY] = CityParcelable.fromCity(City.SAINT_PETERSBURG)
                    showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
                }
        }
    }

    private fun showOnboardingStep(step: OnboardingStep) {
        val steps = onboardingSteps.value
        if (step in steps) {
            savedStateHandle[KEY_CURRENT_ONBOARDING_STEP] = step
        } else {
            Timber.e("There is no step $step in onboarding steps")
        }
    }

    private fun closeOnboarding() {
        viewModelScope.launch {
            val setIsOnboardingCompletedParams =
                SetIsOnboardingCompletedUseCase.Params(isCompleted = true)
            interactor.setIsOnboardingCompleted(setIsOnboardingCompletedParams)
            navigationThrottler.throttle {
                val action = OnboardingScreenAction.OnboardingCompleted(currentCity.value)
                emitSideEffect(SideEffect.NavigateForward(action))
            }
        }
    }

    private fun createOnboardingSteps(): List<OnboardingStep> {
        return buildList {
            OnboardingStep.entries.forEach { step ->
                when (step) {
                    OnboardingStep.NOTIFICATIONS_SETUP -> {
                        val isNotificationsPermissionGranted =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionManager.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                true
                            }
                        if (!isNotificationsPermissionGranted) {
                            add(step)
                        }
                    }

                    else -> add(step)
                }
            }
        }
    }

    sealed interface SideEffect: SideEffectSource.SideEffect {
        data class NavigateForward(val action: OnboardingScreenAction) : SideEffect
    }

    @Parcelize
    enum class OnboardingStep : Parcelable {
        NOTIFICATIONS_SETUP,
        CITY_DETECTION,
        CITY_CONFIRMATION,
    }

    private enum class Operation : OperationKey { DETECT_CITY }

    companion object {
        private const val KEY_ONBOARDING_STEPS = "onboarding_steps"
        private const val KEY_CURRENT_ONBOARDING_STEP = "current_onboarding_step"
        private const val KEY_CURRENT_CITY = "current_city"

        const val ONBOARDING_BANNER_URL = "${BuildConfig.BACKEND_URL}/api/v1/main/splash/"
    }
}
