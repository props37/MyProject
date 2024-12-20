package ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model.LoyaltyProgramEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model.LoyaltyProgramState
import javax.inject.Inject

@HiltViewModel
internal class LoyaltyProgramViewModel @Inject constructor(
    getLoyaltyCardFlow: GetLoyaltyCardFlowUseCase,
) : ViewModel(), SideEffectSource<LoyaltyProgramSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val getLoyaltyCardUseCaseParams =
        GetLoyaltyCardFlowUseCase.Params(CachePolicy.Remote())

    private val loyaltyCardRequester = FlowRequester(LoyaltyCardRequest) {
        getLoyaltyCardFlow(getLoyaltyCardUseCaseParams)
    }

    val loyaltyProgramState: StateFlow<LoyaltyProgramState> = combine(
        loyaltyCardRequester.flow,
        loyaltyCardRequester.loadingState,
    ) { loyaltyCardResult, loadingState ->
        if (loadingState.isLoading()) {
            LoyaltyProgramState.Loading
        } else {
            loyaltyCardResult.fold(
                onSuccess = { card ->
                    if (card != null) {
                        LoyaltyProgramState.Success(card)
                    } else {
                        LoyaltyProgramState.Error(ZarinaErrorScreenState.GENERIC)
                    }
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    LoyaltyProgramState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = LoyaltyProgramState.Loading,
    )

    fun onLoyaltyProgramEvent(event: LoyaltyProgramEvent) {
        when (event) {
            LoyaltyProgramEvent.BackClicked -> onBackClicked()
            LoyaltyProgramEvent.BonusHistoryClicked -> TODO() // TODO: [Top] Implement
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = LoyaltyProgramScreenAction.BackClicked
            emitSideEffect(LoyaltyProgramSideEffect.Navigate(action))
        }
    }

    private data object LoyaltyCardRequest : FlowRequest
}
