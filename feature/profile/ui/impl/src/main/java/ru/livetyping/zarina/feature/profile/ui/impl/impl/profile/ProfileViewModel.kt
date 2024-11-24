package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.buildutil.AppVersionName
import ru.livetyping.zarina.core.buildutil.BuildType
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileMenuItem
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileUserState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.VersionInfo
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    getUserFlowUseCase: GetUserFlowUseCase,
    getLoyaltyCardFlowUseCase: GetLoyaltyCardFlowUseCase,
    getUserCityFlowUseCase: GetUserCityFlowUseCase,
    @AppVersionName
    appVersionName: String,
    appBuildType: BuildType,
) : ViewModel(), SideEffectSource<ProfileSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val getUserUseCaseParams = GetUserFlowUseCase.Params(CachePolicy.LocalOnly)
    private val userState: StateFlow<ProfileUserState> = getUserFlowUseCase(getUserUseCaseParams)
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
    private val loyaltyCard: StateFlow<LoyaltyCard?> =
        getLoyaltyCardFlowUseCase(getLoyaltyCardUseCaseParams)
            .map { it.getOrNull() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    private val getUserCityUseCaseParams =
        GetUserCityFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())
    private val userCity: StateFlow<City?> = getUserCityFlowUseCase(getUserCityUseCaseParams)
        .map { result ->
            result.getOrDefault(City.DEFAULT)
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

    private val versionInfos = buildList {
        val appVersionName = VersionInfo(
            title = Text.Resource(R.string.profile_app_version),
            version = Text.String(appVersionName),
        )
        add(appVersionName)

        if (appBuildType != BuildType.RELEASE) {
            val mindboxDeviceUuid = VersionInfo(
                title = Text.Resource(R.string.profile_mindbox_device_uuid),
                version = Text.String("TODO"), // TODO: [Top] Implement
            )
            add(mindboxDeviceUuid)
        }
    }.toImmutableList()

    val profileState: StateFlow<ProfileState> = combine(
        userState,
        loyaltyCard,
        userCity,
        menuItems,
    ) { userState, loyaltyCard, userCity, menuItems ->
        ProfileState(
            userState = userState,
            loyaltyCard = loyaltyCard,
            userCity = userCity,
            menuItems = menuItems,
            versionInfos = versionInfos,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProfileState(
            userState = ProfileUserState.Loading,
            loyaltyCard = null,
            userCity = null,
            menuItems = ProfileMenuItem.entries.toImmutableList(),
            versionInfos = versionInfos,
        )
    )

    fun onProfileEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.ProfileDetailsClicked -> onProfileDetailsClicked()
            ProfileEvent.SignInClicked -> onSignInClicked()
            ProfileEvent.SignUpClicked -> onSignUpClicked()
            ProfileEvent.LoyaltyCardInfoClicked -> onLoyaltyCardInfoClicked()
            is ProfileEvent.MenuItemClicked -> onMenuItemClicked(event)
        }
    }

    private fun onProfileDetailsClicked() {
        TODO()
        // TODO: [Top] Implement
    }

    private fun onSignInClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.SignInClicked
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }

    private fun onSignUpClicked() {
        TODO()
        // TODO: [Top] Implement
    }

    private fun onLoyaltyCardInfoClicked() {
        TODO()
        // TODO: [Top] Implement
    }

    private fun onMenuItemClicked(event: ProfileEvent.MenuItemClicked) {
        when (event.item) {
            ProfileMenuItem.MyOrders -> TODO()
            ProfileMenuItem.City -> TODO()
            ProfileMenuItem.Stores -> {
                val currentCity = userCity.value
                val action = ProfileScreenAction.ChangeCityClicked(currentCity)
                emitSideEffect(ProfileSideEffect.Navigate(action))
            }

            ProfileMenuItem.Help -> TODO()
            ProfileMenuItem.AboutCompany -> TODO()
        }
        // TODO: [Top] Implement
    }
}
