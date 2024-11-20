package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import androidx.lifecycle.SavedStateHandle
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
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.MenuItem
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.UserState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.VersionDetails
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserFlowUseCase: GetUserFlowUseCase,
    getLoyaltyCardFlowUseCase: GetLoyaltyCardFlowUseCase,
    getUserCityFlowUseCase: GetUserCityFlowUseCase,
    @AppVersionName
    appVersionName: String,
    appBuildType: BuildType,
) : ViewModel(), SideEffectSource<ProfileSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val getUserUseCaseParams = GetUserFlowUseCase.Params(CachePolicy.LocalOnly)
    private val userState: StateFlow<UserState> = getUserFlowUseCase(getUserUseCaseParams)
        .map { result ->
            UserState.Success(user = result.getOrNull())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserState.Loading,
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

    private val menuItems: StateFlow<ImmutableList<MenuItem>> = userState
        .map { userState ->
            val isUserAuthorized = (userState as? UserState.Success)?.user != null
            if (isUserAuthorized) {
                MenuItem.entries.toImmutableList()
            } else {
                MenuItem.entries.minus(MenuItem.MyOrders).toImmutableList()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MenuItem.entries.minus(MenuItem.MyOrders).toImmutableList(),
        )

    private val versionDetails = buildList {
        val appVersionName = VersionDetails(
            title = Text.Resource(R.string.app_version),
            version = Text.String(appVersionName),
        )
        add(appVersionName)

        if (appBuildType != BuildType.RELEASE) {
            val mindboxDeviceUuid = VersionDetails(
                title = Text.Resource(R.string.mindbox_device_uuid),
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
            versionDetails = versionDetails,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProfileState(
            userState = UserState.Loading,
            loyaltyCard = null,
            userCity = null,
            menuItems = MenuItem.entries.toImmutableList(),
            versionDetails = versionDetails,
        )
    )

    fun onProfileEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.ProfileDetailsClicked -> onProfileDetailsClicked()
            is ProfileEvent.MenuItemClicked -> onMenuItemClicked(event)
        }
    }

    private fun onProfileDetailsClicked() {
        TODO()
        // TODO: [Top] Implement
    }

    private fun onMenuItemClicked(event: ProfileEvent.MenuItemClicked) {
        TODO()
        // TODO: [Top] Implement
    }
}
