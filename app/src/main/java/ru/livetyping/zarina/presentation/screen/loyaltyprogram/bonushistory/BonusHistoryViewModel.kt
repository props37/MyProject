package ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.BonusHistoryViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class BonusHistoryViewModel @Inject constructor(
    interactor: BonusHistoryInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val bonusHistoryPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>> =
        interactor.bonusActionPager.getBonusHistoryPagingDataFlow()
            .cachedIn(viewModelScope)

    val expectedBonusesPagingDataFlow: Flow<PagingData<LoyaltyProgramBonusAction>> =
        interactor.bonusActionPager.getExpectedBonusesPagingDataFlow()
            .cachedIn(viewModelScope)

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = BonusHistoryScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: BonusHistoryScreenAction) : SideEffect
    }

    enum class Tab { BONUS_HISTORY, EXPECTED_BONUSES }
}
