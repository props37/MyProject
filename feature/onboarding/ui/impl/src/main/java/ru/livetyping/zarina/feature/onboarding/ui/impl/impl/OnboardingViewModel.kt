package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import android.Manifest
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.permission.PermissionManager
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.feature.onboarding.domain.usecase.GetOnboardingBannerUrlFlowUseCase
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingEvent
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingState
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingStep
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingStepsBuilder
import javax.inject.Inject

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    onboardingStepsBuilder: OnboardingStepsBuilder,
    getOnboardingBannerUrlFlow: GetOnboardingBannerUrlFlowUseCase,
    private val permissionManager: PermissionManager,
) : ViewModel(), SideEffectSource<OnboardingSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val onboardingStepsValueHolder = savedStateHandle.createValueHolder(
        key = Keys.ONBOARDING_STEPS.key,
        initialValue = onboardingStepsBuilder.build(
            isNotificationsPermissionGranted = isNotificationsPermissionGranted(),
        ),
    )

    private val currentOnboardingStep = savedStateHandle.createValueHolder(
        key = Keys.CURRENT_ONBOARDING_STEP.key,
        initialValue = onboardingStepsValueHolder.get()?.firstOrNull()
            ?: OnboardingStep.CITY_DETECTION,
    )

    val onboardingState: StateFlow<OnboardingState> = combine(
        onboardingStepsValueHolder.stateFlow,
        currentOnboardingStep.stateFlow,
    ) { onboardingSteps, currentStep ->
        OnboardingState(
            onboardingSteps = onboardingSteps.toImmutableList(),
            currentOnboardingStep = currentStep,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = OnboardingState(
            onboardingSteps = onboardingStepsValueHolder.stateFlow.value.toImmutableList(),
            currentOnboardingStep = currentOnboardingStep.stateFlow.value,
        ),
    )

    val bannerUrl: StateFlow<Url?> = flow {
        val urlFlow = getOnboardingBannerUrlFlow().map { it.getOrNull() }
        emitAll(urlFlow)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = null,
    )

    fun onOnboardingEvent(event: OnboardingEvent) {
        // TODO: [Top] Implement
    }

    private fun isNotificationsPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }
    }

    private enum class Keys {
        ONBOARDING_STEPS,
        CURRENT_ONBOARDING_STEP;

        val key: String get() = name
    }
}
