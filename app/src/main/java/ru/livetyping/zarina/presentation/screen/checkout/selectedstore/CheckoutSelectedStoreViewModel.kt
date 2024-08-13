package ru.livetyping.zarina.presentation.screen.checkout.selectedstore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.store.StoreParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedstore.CheckoutSelectedStoreViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class CheckoutSelectedStoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutSelectedStoreInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartType: StateFlow<CartType> = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = CheckoutGraph.SelectedStore.ARG_KEY_CART_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "cartType is null" }
            it.toCartType()
        }

    private val step: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CheckoutGraph.SelectedStore.ARG_KEY_STEP,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "step is null" }
        }

    val store: StateFlow<Store> = savedStateHandle
        .getStateFlow<StoreParcelable?>(
            key = CheckoutGraph.SelectedStore.ARG_KEY_STORE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "store is null" }
            it.toStore()
        }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutSelectedStoreScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutSelectedStoreScreenAction) : SideEffect
    }
}
