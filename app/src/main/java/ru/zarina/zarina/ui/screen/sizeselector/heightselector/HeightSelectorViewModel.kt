package ru.zarina.zarina.ui.screen.sizeselector.heightselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorViewModel.SideEffect
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class HeightSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val product: StateFlow<Product> = savedStateHandle
        .getStateFlow<ProductParcelable?>(
            key = UnscopedDestinations.HeightSelector.ARG_KEY_PRODUCT,
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
            key = UnscopedDestinations.HeightSelector.ARG_KEY_OFFERS,
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
            val result = HeightSelectorScreenResult.ScreenClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val result = HeightSelectorScreenResult.SizeSelectorFlowClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onOfferClicked(offer: ProductOffer) {
        navigationThrottler.throttle {
            val action = HeightSelectorScreenAction.OfferClicked(product.value, offer)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: HeightSelectorScreenAction) : SideEffect
        data class NavigateBackward(val result: HeightSelectorScreenResult) : SideEffect
    }
}
