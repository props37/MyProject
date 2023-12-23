package ru.zarina.zarina.ui.screen.onboarding

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.data.permissionmanager.isDenied
import ru.zarina.zarina.data.permissionmanager.isGranted
import ru.zarina.zarina.data.permissionmanager.shouldShowRequestRationale
import ru.zarina.zarina.domain.rework.OnboardingStep
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModelRework.SideEffect
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModelRework @Inject constructor(
    private val interactor: OnboardingInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val onboardingSteps = OnboardingStepsBuilder.build(interactor.permissionManager)

    private val currentOnboardingStepIndex = MutableStateFlow(0)

    private val permissionManager = interactor.permissionManager

    val onboarding = currentOnboardingStepIndex
        .map { currentStepIndex ->
            val coercedStepIndex = currentStepIndex.coerceIn(0, onboardingSteps.lastIndex)
            val currentStep = onboardingSteps[coercedStepIndex]
            val currentPage = Onboarding.Page(number = coercedStepIndex + 1, step = currentStep)
            Onboarding(currentPage = currentPage, pageCount = onboardingSteps.size)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Onboarding(
                currentPage = Onboarding.Page(
                    number = currentOnboardingStepIndex.value + 1,
                    step = onboardingSteps.first(),
                ),
                pageCount = onboardingSteps.size,
            ),
        )

    fun onRequestNotificationsPermissionClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            viewModelScope.launch {
                val currentPermissionState = permissionManager.getPermissionState(permission)
                if (currentPermissionState.isGranted) {
                    showNextOnboardingPage()
                } else {
                    val newPermissionState = permissionManager.requestPermission(permission)
                    if (newPermissionState != currentPermissionState) {
                        // User has either granted or denied the permission
                        showNextOnboardingPage()
                    } else if (
                        newPermissionState.isDenied && !newPermissionState.shouldShowRequestRationale
                    ) {
                        val hasPermissionRequiredRequestRationale =
                            permissionManager.hasPermissionRequiredRequestRationale(permission)
                                .firstOrNull() ?: false
                        if (hasPermissionRequiredRequestRationale) {
                            // User has denied the permission permanently
                            showNextOnboardingPage()
                        }
                    }
                }
            }
        } else {
            showNextOnboardingPage()
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

    private fun showNextOnboardingPage() {
        if (currentOnboardingStepIndex.value != onboardingSteps.lastIndex) {
            currentOnboardingStepIndex.value += 1
        } else {
            // TODO: [High] Close onboarding
        }
    }

    @Immutable
    data class Onboarding(
        val currentPage: Page,
        val pageCount: Int,
    ) {
        @Immutable
        data class Page(
            val number: Int,
            val step: OnboardingStep,
        )
    }

    sealed interface SideEffect: SideEffectSource.SideEffect
}
