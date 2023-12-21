package ru.zarina.zarina.ui.screen.onboarding

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.data.permissionmanager.isGranted
import ru.zarina.zarina.domain.rework.OnboardingStep
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModelRework.SideEffect
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModelRework @Inject constructor(
    private val interactor: OnboardingInteractorRework,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val onboardingSteps = OnboardingStepsBuilder.build(interactor.permissionManager)

    private val currentOnboardingStepIndex = MutableStateFlow(0)

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
            val currentPermissionState = interactor.permissionManager.getPermissionState(permission)
            if (currentPermissionState.isGranted) {
                showNextOnboardingPage()
            } else {
                viewModelScope.launch {
                    val newPermissionState =
                        interactor.permissionManager.requestPermission(permission)
                    if (newPermissionState != currentPermissionState) {
                        showNextOnboardingPage()
                    }
                }
            }
        } else {
            showNextOnboardingPage()
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
