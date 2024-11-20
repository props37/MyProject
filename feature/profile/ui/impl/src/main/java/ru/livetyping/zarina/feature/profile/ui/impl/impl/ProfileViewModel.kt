package ru.livetyping.zarina.feature.profile.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.feature.profile.ui.impl.impl.model.ProfileState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.model.UserState
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserFlowUseCase: GetUserFlowUseCase,
    getLoyaltyCardFlowUseCase: GetLoyaltyCardFlowUseCase,
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

    val profileState: StateFlow<ProfileState> = TODO()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileScreenAction.ScreenClosed
            emitSideEffect(ProfileSideEffect.Navigate(action))
        }
    }
}
