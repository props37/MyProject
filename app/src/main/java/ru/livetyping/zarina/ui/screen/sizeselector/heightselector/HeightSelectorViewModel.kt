package ru.livetyping.zarina.ui.screen.sizeselector.heightselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.model.product.ProductOfferParcelable
import ru.livetyping.zarina.ui.model.product.ProductParcelable
import ru.livetyping.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.ui.screen.sizeselector.heightselector.HeightSelectorViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class HeightSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val product: StateFlow<Product> = savedStateHandle
        .getStateFlow<ProductParcelable?>(
            key = SizeSelectorGraph.HeightSelector.ARG_KEY_PRODUCT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "product is null" }
            parcelable.toProduct()
        }

    val offers: StateFlow<ImmutableList<ProductOffer>> = savedStateHandle
        .getStateFlow<Array<ProductOfferParcelable>?>(
            key = SizeSelectorGraph.HeightSelector.ARG_KEY_OFFERS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelables ->
            checkNotNull(parcelables) { "offers is null" }
            parcelables
                .map { it.toProductOffer() }
                .toImmutableList()
        }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = HeightSelectorScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = HeightSelectorScreenAction.SizeSelectorFlowClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOfferClicked(offer: ProductOffer) {
        navigationThrottler.throttle {
            val action = HeightSelectorScreenAction.OfferClicked(product.value, offer)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: HeightSelectorScreenAction) : SideEffect
    }
}
