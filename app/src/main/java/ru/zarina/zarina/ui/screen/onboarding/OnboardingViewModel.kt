package ru.zarina.zarina.ui.screen.onboarding

import android.Manifest
import android.os.Build
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.data.permissionmanager.isDenied
import ru.zarina.zarina.data.permissionmanager.isGranted
import ru.zarina.zarina.data.permissionmanager.shouldShowRequestRationale
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: OnboardingInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val permissionManager = interactor.permissionManager

    val onboardingSteps: StateFlow<List<OnboardingStep>> = savedStateHandle.getStateFlow(
        key = KEY_ONBOARDING_STEPS,
        initialValue = createOnboardingSteps(),
    )

    val currentOnboardingStep: StateFlow<OnboardingStep> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_ONBOARDING_STEP,
        initialValue = onboardingSteps.value.firstOrNull() ?: OnboardingStep.CITY_DETECTION,
    )

    fun onRequestNotificationsPermissionClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            viewModelScope.launch {
                val currentPermissionState = permissionManager.getPermissionState(permission)
                if (currentPermissionState.isGranted) {
                    showNextOnboardingStep()
                } else {
                    val newPermissionState = permissionManager.requestPermission(permission)
                    if (newPermissionState != currentPermissionState) {
                        // User has either granted or denied the permission
                        showNextOnboardingStep()
                    } else if (
                        newPermissionState.isDenied && !newPermissionState.shouldShowRequestRationale
                    ) {
                        val hasPermissionRequiredRequestRationale =
                            permissionManager.hasPermissionRequiredRequestRationale(permission)
                                .firstOrNull() ?: false
                        if (hasPermissionRequiredRequestRationale) {
                            // User has denied the permission permanently
                            showNextOnboardingStep()
                        }
                    }
                }
            }
        } else {
            showNextOnboardingStep()
        }
    }

    fun onDetectCityClicked() {
        val permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        viewModelScope.launch {
            val currentPermissionsState = permissionManager.getMultiplePermissionsState(permissions)
            if (currentPermissionsState.any { it.value.isGranted }) {
                // TODO: [High] Detect city
            } else {
                val newPermissionsState = permissionManager.requestMultiplePermissions(permissions)
                if (newPermissionsState != currentPermissionsState) {
                    // User has either granted or denied the permission
                    if (newPermissionsState.any { it.value.isGranted }) {
                        // TODO: [High] Detect city
                    } else {
                        // TODO: [High] Skip city detection
                    }
                } else if (
                    // TODO: [High] Check
                    newPermissionsState.all { it.value.isDenied }
                    && newPermissionsState.any { !it.value.shouldShowRequestRationale }
                ) {
                    val havePermissionsRequiredRequestRationale =
                        permissionManager.haveMultiplePermissionsRequiredRequestRationale(permissions)
                            .firstOrNull() ?: emptyMap()
                    if (havePermissionsRequiredRequestRationale.any { it.value == true }) {
                        // User has denied the permission permanently
                        // TODO: [High] Detect default city
                    }
                }
            }
        }
    }

    private fun showNextOnboardingStep() {
        val steps = onboardingSteps.value
        val currentStep = currentOnboardingStep.value
        val currentStepIndex = steps.indexOf(currentStep)
        val nextStepIndex = currentStepIndex + 1
        if (nextStepIndex <= steps.lastIndex) {
            val nextStep = steps[nextStepIndex]
            savedStateHandle[KEY_CURRENT_ONBOARDING_STEP] = nextStep
        } else {
            // TODO: [High] Close onboarding
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

    sealed interface SideEffect: SideEffectSource.SideEffect

    @Parcelize
    enum class OnboardingStep : Parcelable {
        NOTIFICATIONS_SETUP,
        CITY_DETECTION,
        CITY_CONFIRMATION,
    }

    companion object {
        private const val KEY_ONBOARDING_STEPS = "onboarding_steps"
        private const val KEY_CURRENT_ONBOARDING_STEP = "current_onboarding_step"
    }
}
