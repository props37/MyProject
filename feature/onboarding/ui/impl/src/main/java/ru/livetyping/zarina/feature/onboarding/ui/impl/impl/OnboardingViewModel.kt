package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.usecase.geo.GetCurrentCityByLocationFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.GetOnboardingBannerUrlFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.SetIsOnboardingCompletedUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetLocalUserCityUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.core.permission.PermissionManager
import ru.livetyping.zarina.core.permission.shouldShowRequestRationale
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.createValueHolder
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingEvent
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingState
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingStep
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingStepsBuilder
import timber.log.Timber
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val permissionManager: PermissionManager,
    onboardingStepsBuilder: OnboardingStepsBuilder,
    getOnboardingBannerUrlFlow: GetOnboardingBannerUrlFlowUseCase,
    private val getCurrentCityByLocationFlow: GetCurrentCityByLocationFlowUseCase,
    private val setIsOnboardingCompleted: SetIsOnboardingCompletedUseCase,
    private val setUserCity: SetUserCityUseCase,
    private val setLocalUserCity: SetLocalUserCityUseCase,
) : ViewModel(), SideEffectSource<OnboardingSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var detectCityJob: Job? = null
    private var completeOnboardingJob: Job? = null

    private val onboardingStepsValueHolder = savedStateHandle.createValueHolder(
        key = Keys.ONBOARDING_STEPS.key,
        initialValue = onboardingStepsBuilder.build(
            isNotificationsPermissionGranted = isNotificationsPermissionGranted(),
        ),
    )

    private val currentOnboardingStepValueHolder = savedStateHandle.createValueHolder(
        key = Keys.CURRENT_ONBOARDING_STEP.key,
        initialValue = onboardingStepsValueHolder.get().firstOrNull()
            ?: OnboardingStep.CITY_DETECTION,
    )

    private val cityValueHolder = savedStateHandle.createValueHolder<CityParcelable?>(
        key = Keys.CITY.key,
        initialValue = null,
    )

    private val onboardingCompletionTrigger = MutableStateFlow<OnboardingCompletionTrigger?>(null)

    val onboardingState: StateFlow<OnboardingState> = combine(
        onboardingStepsValueHolder.stateFlow,
        currentOnboardingStepValueHolder.stateFlow,
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
            currentOnboardingStep = currentOnboardingStepValueHolder.stateFlow.value,
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
        when (event) {
            OnboardingEvent.RequestNotificationsPermissionClicked -> {
                onRequestNotificationsPermissionClicked()
            }

            OnboardingEvent.DetectCityClicked -> onDetectCityClicked()
            OnboardingEvent.SkipCityDetectionClicked -> onSkipCityDetectionClicked()
            OnboardingEvent.ConfirmCityClicked -> onConfirmCityClicked()
            OnboardingEvent.SelectCityClicked -> onSelectCityClicked()
        }
    }

    private fun onRequestNotificationsPermissionClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onRequestNotificationsPermissionClickedApi33()
        } else {
            showOnboardingStep(OnboardingStep.CITY_DETECTION)
        }
    }

    private fun onDetectCityClicked() {
        if (detectCityJob?.isActive == true) return

        detectCityJob = viewModelScope.launch {
            val currentPermissionsState =
                permissionManager.getMultiplePermissionsState(LOCATION_PERMISSIONS)
            if (currentPermissionsState.any { it.value.isGranted }) {
                detectCity()
            } else {
                val newPermissionsState =
                    permissionManager.requestMultiplePermissions(LOCATION_PERMISSIONS)
                if (newPermissionsState != currentPermissionsState) {
                    // User has either granted or denied the permission
                    if (newPermissionsState.any { it.value.isGranted }) {
                        detectCity()
                    } else {
                        skipCityDetection()
                    }
                } else if (
                    newPermissionsState.all { it.value.isDenied }
                    && newPermissionsState.any { !it.value.shouldShowRequestRationale }
                ) {
                    val havePermissionsRequiredRequestRationale =
                        permissionManager
                            .haveMultiplePermissionsRequiredRequestRationale(LOCATION_PERMISSIONS)
                            .firstOrNull() ?: emptyMap()
                    if (havePermissionsRequiredRequestRationale.any { it.value == true }) {
                        // User has denied the permission permanently
                        cityValueHolder.set(CityParcelable.from(City.DEFAULT))
                        showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
                    }
                }
            }
        }
    }

    private fun onSkipCityDetectionClicked() {
        skipCityDetection()
    }

    private fun onConfirmCityClicked() {
        if (completeOnboardingJob?.isActive == true) return

        onboardingCompletionTrigger.value = OnboardingCompletionTrigger.CITY_CONFIRMED
        completeOnboardingJob = viewModelScope.launch {
            val city = cityValueHolder.get()?.toCity() ?: run {
                Timber.e("User city is null, proceeding with default")
                City.DEFAULT
            }
            completeOnboarding(city)
                .onSuccess {
                    val action = OnboardingScreenAction.OnboardingCompleted(city)
                    emitSideEffect(OnboardingSideEffect.Navigate(action))
                }
                .onFailure {
                    val text = Text.Resource(RCommon.string.something_went_wrong)
                    emitSideEffect(OnboardingSideEffect.ShowToast(text))

                    val action = OnboardingScreenAction.OnboardingCompleted(selectedCity = null)
                    emitSideEffect(OnboardingSideEffect.Navigate(action))
                }
        }
    }

    private fun onSelectCityClicked() {
        if (completeOnboardingJob?.isActive == true) return

        navigationThrottler.throttle {
            val action = OnboardingScreenAction.SelectCityClicked(cityValueHolder.get()?.toCity())
            emitSideEffect(OnboardingSideEffect.Navigate(action))
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun onRequestNotificationsPermissionClickedApi33() {
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
    }

    private suspend fun detectCity() {
        operationTracker.track(Operation.DETECT_CITY) {
            getCurrentCityByLocationFlow().firstOrNull()
                ?.onSuccess { city ->
                    val cityParcelable = city?.let { CityParcelable.from(it) }
                    cityValueHolder.set(cityParcelable)
                    showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
                }
                ?.onFailure { onDetectCityFailure() }
                ?.let { onDetectCityFailure() }
        }
    }

    private fun onDetectCityFailure() {
        cityValueHolder.set(CityParcelable.from(City.DEFAULT))
        showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
    }

    private fun skipCityDetection() {
        if (completeOnboardingJob?.isActive == true) return

        detectCityJob?.cancel()
        onboardingCompletionTrigger.value = OnboardingCompletionTrigger.CITY_DETECTION_SKIPPED
        completeOnboardingJob = viewModelScope.launch {
            completeOnboarding(selectedCity = null)
            val action = OnboardingScreenAction.OnboardingCompleted(selectedCity = null)
            emitSideEffect(OnboardingSideEffect.Navigate(action))
        }
    }

    private suspend fun completeOnboarding(selectedCity: City?): Result<Unit> {
        return operationTracker.track(Operation.COMPLETE_ONBOARDING) {
            val setIsOnboardingCompletedParams =
                SetIsOnboardingCompletedUseCase.Params(isCompleted = true)
            setIsOnboardingCompleted(setIsOnboardingCompletedParams)

            val setUserCityParams = SetUserCityUseCase.Params(selectedCity ?: City.DEFAULT)
            setUserCity(setUserCityParams)
                .onFailure {
                    setLocalUserCity(SetLocalUserCityUseCase.Params(City.DEFAULT))
                }
        }
    }

    private fun showOnboardingStep(step: OnboardingStep) {
        val steps = onboardingStepsValueHolder.get()
        if (step in steps) {
            currentOnboardingStepValueHolder.set(step)
        } else {
            Timber.e("There is no step $step in the onboarding steps")
        }
    }

    private fun isNotificationsPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionManager.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            true
        }
    }

    private enum class OnboardingCompletionTrigger {
        CITY_DETECTION_SKIPPED,
        CITY_CONFIRMED,
    }

    private enum class Operation : OperationKey { DETECT_CITY, COMPLETE_ONBOARDING }

    private enum class Keys {
        ONBOARDING_STEPS,
        CURRENT_ONBOARDING_STEP,
        CITY;

        val key: String get() = name
    }

    private companion object {
        private val LOCATION_PERMISSIONS: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}
