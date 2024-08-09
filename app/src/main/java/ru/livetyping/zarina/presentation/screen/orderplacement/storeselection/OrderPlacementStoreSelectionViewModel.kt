package ru.livetyping.zarina.presentation.screen.orderplacement.storeselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.orderplacement.storeselection.OrderPlacementStoreSelectionViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class OrderPlacementStoreSelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: OrderPlacementStoreSelectionInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OrderPlacementStoreSelectionScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = OrderPlacementStoreSelectionScreenAction.OrderPlacementClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: OrderPlacementStoreSelectionScreenAction) : SideEffect
    }
}
