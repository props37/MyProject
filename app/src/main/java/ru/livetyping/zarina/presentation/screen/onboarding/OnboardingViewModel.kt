package ru.livetyping.zarina.presentation.screen.onboarding

import android.Manifest
import android.os.Build
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.permissionmanager.isDenied
import ru.livetyping.zarina.presentation.common.permissionmanager.isGranted
import ru.livetyping.zarina.presentation.common.permissionmanager.shouldShowRequestRationale
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.onboarding.OnboardingViewModel.SideEffect
import ru.livetyping.zarina.usecase.device.SetIsOnboardingCompletedUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import timber.log.Timber
import kotlin.coroutines.coroutineContext

@HiltViewModel(assistedFactory = OnboardingViewModel.Factory::class)
class OnboardingViewModel @AssistedInject constructor(
    @Assisted
    private val citySelectorResultFlow: StateFlow<UnscopedDestinations.CitySelector.Result?>,
    private val savedStateHandle: SavedStateHandle,
    private val interactor: OnboardingInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val permissionManager = interactor.permissionManager

    private var detectCityJob: Job? = null
    private var completeOnboardingJob: Job? = null

    private val onboardingCompletionTrigger = MutableStateFlow<OnboardingCompletionTrigger?>(null)

    val bannerUrl: StateFlow<Url?> = flow {
        val url = interactor.getOnboardingBannerUrl().getOrNull()
        emit(url)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = null,
    )

    val onboardingSteps: StateFlow<ImmutableList<OnboardingStep>> = savedStateHandle
        .getStateFlow(
            key = KEY_ONBOARDING_STEPS,
            initialValue = createOnboardingSteps(),
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it.toImmutableList() }

    val currentOnboardingStep: StateFlow<OnboardingStep> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_ONBOARDING_STEP,
        initialValue = onboardingSteps.value.firstOrNull() ?: OnboardingStep.CITY_DETECTION,
    )

    val userCity: StateFlow<City?> = savedStateHandle
        .getStateFlow<CityParcelable?>(
            key = KEY_CURRENT_CITY,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it?.toCity() }

    val isSkipCityDetectionButtonLoading: StateFlow<Boolean> = combine(
        operationTracker.isOperationOngoing(Operation.COMPLETE_ONBOARDING),
        onboardingCompletionTrigger,
    ) { isOnboardingBeingCompleted, onboardingCompletionTrigger ->
        isOnboardingBeingCompleted
                && onboardingCompletionTrigger == OnboardingCompletionTrigger.CITY_DETECTION_SKIPPED
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = false,
    )

    val isDetectCityButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.DETECT_CITY)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    val isConfirmCityButtonLoading: StateFlow<Boolean> = combine(
        operationTracker.isOperationOngoing(Operation.COMPLETE_ONBOARDING),
        onboardingCompletionTrigger,
    ) { isOnboardingBeingCompleted, onboardingCompletionTrigger ->
        isOnboardingBeingCompleted &&
                onboardingCompletionTrigger == OnboardingCompletionTrigger.CITY_CONFIRMED
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = false,
    )

    init {
        handleCitySelectorResult()
    }

    fun onRequestNotificationsPermissionClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            viewModelScope.launch {
                val currentPermissionState = permissionManager.getPermissionState(permission)
                if (currentPermissionState.isGranted) {
                    showOnboardingStep(OnboardingStep.CITY_DETECTION)
                    emitSideEffect(SideEffect.NotificationPermissionGranted)
                } else {
                    val newPermissionState = permissionManager.requestPermission(permission)
                    if (newPermissionState.isGranted) {
                        emitSideEffect(SideEffect.NotificationPermissionGranted)
                    }
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
                        savedStateHandle[KEY_CURRENT_CITY] = CityParcelable.from(City.DEFAULT)
                        showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
                    }
                }
            }
        }
    }

    fun onSkipCityDetectionClicked() {
        skipCityDetection()
    }

    fun onConfirmCityClicked() {
        if (completeOnboardingJob?.isActive == true) return

        onboardingCompletionTrigger.value = OnboardingCompletionTrigger.CITY_CONFIRMED
        completeOnboardingJob = viewModelScope.launch {
            val userCity = userCity.value ?: run {
                Timber.e("User city is null, proceeding with default")
                City.DEFAULT
            }
            completeOnboarding(userCity)
                .onSuccess {
                    val action = OnboardingScreenAction.OnboardingCompleted(userCity)
                    emitSideEffect(SideEffect.NavigateForward(action))
                }
                .onFailure {
                    val text = Text.Resource(R.string.something_went_wrong)
                    emitSideEffect(SideEffect.ShowToast(text))

                    val action = OnboardingScreenAction.OnboardingCompleted(userCity = null)
                    emitSideEffect(SideEffect.NavigateForward(action))
                }
        }
    }

    fun onSelectCityClicked() {
        if (completeOnboardingJob?.isActive == true) return

        navigationThrottler.throttle {
            val action = OnboardingScreenAction.SelectCityClicked(userCity.value)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    private suspend fun detectCity() {
        fun onFailure() {
            savedStateHandle[KEY_CURRENT_CITY] = CityParcelable.from(City.DEFAULT)
            showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
        }

        operationTracker.track(Operation.DETECT_CITY) {
            interactor.getCurrentCityFlow().firstOrNull()
                ?.onSuccess { city ->
                    savedStateHandle[KEY_CURRENT_CITY] = city?.let { CityParcelable.from(it) }
                    showOnboardingStep(OnboardingStep.CITY_CONFIRMATION)
                }
                ?.onFailure { onFailure() }
                ?.let { onFailure() }
        }
    }

    private fun skipCityDetection() {
        if (completeOnboardingJob?.isActive == true) return

        detectCityJob?.cancel()
        onboardingCompletionTrigger.value = OnboardingCompletionTrigger.CITY_DETECTION_SKIPPED
        completeOnboardingJob = viewModelScope.launch {
            completeOnboarding(userCity = null)
            val action = OnboardingScreenAction.OnboardingCompleted(userCity = null)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    private fun showOnboardingStep(step: OnboardingStep) {
        val steps = onboardingSteps.value
        if (step in steps) {
            savedStateHandle[KEY_CURRENT_ONBOARDING_STEP] = step
        } else {
            Timber.e("There is no step $step in the onboarding steps")
        }
    }

    private suspend fun completeOnboarding(userCity: City?): Result<Unit> {
        return operationTracker.track(Operation.COMPLETE_ONBOARDING) {
            val setIsOnboardingCompletedParams =
                SetIsOnboardingCompletedUseCase.Params(isCompleted = true)
            interactor.setIsOnboardingCompleted(setIsOnboardingCompletedParams)

            val setUserCityParams = SetUserCityUseCase.Params(userCity ?: City.DEFAULT)
            val result = interactor.setUserCity(setUserCityParams)
            result.onFailure { interactor.setDefaultUserCity() }
            coroutineContext.ensureActive()
            result
        }
    }

    private fun handleCitySelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.CitySelector.Result>(
                resultFlow = citySelectorResultFlow,
                key = KEY_RESULT_CITY_SELECTOR,
            ) { result ->
                savedStateHandle[KEY_CURRENT_CITY] = result.city
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

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: OnboardingScreenAction) : SideEffect

        data class ShowToast(val message: Text) : SideEffect

        data object NotificationPermissionGranted : SideEffect
    }

    @Parcelize
    enum class OnboardingStep : Parcelable {
        NOTIFICATIONS_SETUP,
        CITY_DETECTION,
        CITY_CONFIRMATION,
    }

    private enum class OnboardingCompletionTrigger {
        CITY_DETECTION_SKIPPED,
        CITY_CONFIRMED,
    }

    private enum class Operation : OperationKey { DETECT_CITY, COMPLETE_ONBOARDING }

    @AssistedFactory
    interface Factory {
        fun create(
            citySelectorResultFlow: StateFlow<UnscopedDestinations.CitySelector.Result?>,
        ): OnboardingViewModel
    }

    companion object {
        private const val KEY_ONBOARDING_STEPS = "onboarding_steps"
        private const val KEY_CURRENT_ONBOARDING_STEP = "current_onboarding_step"
        private const val KEY_CURRENT_CITY = "current_city"

        private const val KEY_RESULT_CITY_SELECTOR = "result_city_selector"

        private val LOCATION_PERMISSIONS: List<String>
            get() = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
    }
}
