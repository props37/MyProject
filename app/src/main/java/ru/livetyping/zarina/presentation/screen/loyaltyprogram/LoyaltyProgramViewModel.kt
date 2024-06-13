package ru.livetyping.zarina.presentation.screen.loyaltyprogram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.user.LoyaltyCard
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramViewModel.SideEffect
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class LoyaltyProgramViewModel @Inject constructor(
    interactor: LoyaltyProgramInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val loyaltyCard: StateFlow<LoyaltyCard?> = interactor.getLoyaltyCardFlow()
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = LoyaltyProgramScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onLoyaltyProgramPoliciesClicked() {
        navigationThrottler.throttle {
            val url = Text.Resource(R.string.loyalty_policy_url)
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    fun onBonusHistoryClicked() {
        navigationThrottler.throttle {
            val action = LoyaltyProgramScreenAction.BonusHistoryClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: LoyaltyProgramScreenAction) : SideEffect

        data class OpenUrl(val url: Text) : SideEffect
    }
}

