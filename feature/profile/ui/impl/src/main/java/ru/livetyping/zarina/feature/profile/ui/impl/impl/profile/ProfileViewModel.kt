package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.core.navigationutil.ScreenResultHandler
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.feature.profile.ui.ProfileSelectedCityResult
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileMenuItem
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileUserState
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
internal class ProfileViewModel @AssistedInject constructor(
    @Assisted
    selectedCityResultFlow: Flow<ProfileSelectedCityResult?>,
    savedStateHandle: SavedStateHandle,
    private val deps: ProfileDependencies,
) : ViewModel(), SideEffectSource<ProfileSideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val getUserUseCaseParams = GetUserFlowUseCase.Params(CachePolicy.LocalOnly)
    private val userState: StateFlow<ProfileUserState> = deps.getUserFlow(getUserUseCaseParams)
        .map { result ->
            ProfileUserState.Success(user = result.getOrNull())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ProfileUserState.Loading,
        )

    private val getLoyaltyCardUseCaseParams =
        GetLoyaltyCardFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())
    private val loyaltyCardFlow = deps.getLoyaltyCardFlow(getLoyaltyCardUseCaseParams)
        .map { it.getOrNull() }

    private val getUserCityUseCaseParams =
        GetUserCityFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())
    private val userCity: StateFlow<City?> = deps.getUserCityFlow(getUserCityUseCaseParams)
        .map { result ->
            result.getOrDefault(City.getDefault())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val menuItems: StateFlow<ImmutableList<ProfileMenuItem>> = userState
        .map { userState ->
            val isUserAuthorized = (userState as? ProfileUserState.Success)?.user != null
            if (isUserAuthorized) {
                ProfileMenuItem.entries.toImmutableList()
            } else {
                ProfileMenuItem.entries.minus(ProfileMenuItem.MyOrders).toImmutableList()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ProfileMenuItem.entries.minus(ProfileMenuItem.MyOrders).toImmutableList(),
        )

    val profileState: StateFlow<ProfileState> = combine(
        userState,
        loyaltyCardFlow,
        userCity,
        menuItems,
    ) { userState, loyaltyCard, userCity, menuItems ->
        ProfileState(
            userState = userState,
            loyaltyCard = loyaltyCard,
            userCity = userCity,
            menuItems = menuItems,
            buildInfo = getBuildInfo(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProfileState(
            userState = ProfileUserState.Loading,
            loyaltyCard = null,
            userCity = null,
            menuItems = ProfileMenuItem.entries.toImmutableList(),
            buildInfo = getBuildInfo(),
        )
    )

    init {
        handleSelectedCityResult(selectedCityResultFlow)
    }

    fun onProfileEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.ProfileDetailsClicked -> onProfileDetailsClicked()
            ProfileEvent.SignInClicked -> onSignInClicked()
            ProfileEvent.SignUpClicked -> onSignUpClicked()
            ProfileEvent.LoyaltyCardInfoClicked -> onLoyaltyCardInfoClicked()
            is ProfileEvent.MenuItemClicked -> onMenuItemClicked(event)
        }
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.BackClicked
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }

    private fun onProfileDetailsClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.ProfileDetailsClicked
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }

    private fun onSignInClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.SignInClicked
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }

    private fun onSignUpClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.SignUpClicked
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }

    private fun onLoyaltyCardInfoClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.LoyaltyCardInfoClicked
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }

    private fun onMenuItemClicked(event: ProfileEvent.MenuItemClicked) {
        when (event.item) {
            ProfileMenuItem.MyOrders -> {
                navigationThrottler.throttle {
                    val action = ProfileScreenAction.MyOrdersClicked
                    emitSideEffect(ProfileSideEffect.Navigate(action))
                }
            }

            ProfileMenuItem.City -> {
                navigationThrottler.throttle {
                    val currentCity = userCity.value
                    val action = ProfileScreenAction.ChangeCityClicked(currentCity)
                    emitSideEffect(ProfileSideEffect.Navigate(action))
                }
            }

            ProfileMenuItem.Stores -> {
                navigationThrottler.throttle {
                    val action = ProfileScreenAction.StoresClicked
                    emitSideEffect(ProfileSideEffect.Navigate(action))
                }
            }

            ProfileMenuItem.Help -> {
                navigationThrottler.throttle {
                    val url = Text.Resource(RCommon.string.res_zarina_help_url)
                    emitSideEffect(ProfileSideEffect.OpenUrl(url))
                }
            }

            ProfileMenuItem.AboutCompany -> {
                navigationThrottler.throttle {
                    val url = Text.Resource(RCommon.string.res_zarina_about_company_url)
                    emitSideEffect(ProfileSideEffect.OpenUrl(url))
                }
            }
        }
    }

    private fun handleSelectedCityResult(resultFlow: Flow<ProfileSelectedCityResult?>) {
        viewModelScope.launch {
            screenResultHandler.handle(
                resultFlow = resultFlow,
                key = Keys.SELECTED_CITY_RESULT.key,
            ) { result ->
                updateUserCity(result.city)
            }
        }
    }

    private suspend fun updateUserCity(city: City) {
        val params = SetUserCityUseCase.Params(city)
        deps.setUserCity(params)
            .onFailure {
                val text = Text.Resource(R.string.profile_city_changing_error)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(ProfileSideEffect.ShowZarinaToast(message))
            }
    }

    private fun getBuildInfo(): ProfileState.BuildInfo {
        return ProfileState.BuildInfo(
            appVersion = deps.appVersionName,
            mindboxDeviceUuid = deps.mindboxDeviceUuidProvider.getDeviceUuid(),
        )
    }

    private enum class Keys {
        SELECTED_CITY_RESULT;

        val key: String get() = name
    }

    @AssistedFactory
    internal interface Factory {
        fun create(selectedCityResultFlow: Flow<ProfileSelectedCityResult?>): ProfileViewModel
    }
}
