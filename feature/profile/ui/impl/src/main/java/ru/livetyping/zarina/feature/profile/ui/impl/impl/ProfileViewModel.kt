package ru.livetyping.zarina.feature.profile.ui.impl.impl

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
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.feature.profile.ui.impl.impl.model.MenuItem
import ru.livetyping.zarina.feature.profile.ui.impl.impl.model.ProfileState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.model.UserState
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserFlowUseCase: GetUserFlowUseCase,
    getLoyaltyCardFlowUseCase: GetLoyaltyCardFlowUseCase,
    getUserCityFlowUseCase: GetUserCityFlowUseCase,
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
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProfileState(
            userState = UserState.Loading,
            loyaltyCard = null,
            userCity = null,
            menuItems = MenuItem.entries.toImmutableList(),
        )
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.ScreenClosed
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }
}
