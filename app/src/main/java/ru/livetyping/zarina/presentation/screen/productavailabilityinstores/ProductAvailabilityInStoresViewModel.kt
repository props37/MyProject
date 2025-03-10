package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductAvailabilityInStoresViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<UnscopedDestinations.ProductAvailabilityInStores>(
        typeMap = UnscopedDestinations.ProductAvailabilityInStores.typeMap(),
    )
    private val product = navEntry.product.toProductItem()

    private val selectedOfferBarcode: MutableStateFlow<Barcode?> = MutableStateFlow(
        value = getInitiallySelectedOfferBarcode(product.offers),
    )

    val offers: StateFlow<ImmutableList<OfferItem>> = selectedOfferBarcode.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { selectedOfferBarcode ->
        val heightSet = product.offers.mapTo(mutableSetOf()) { it.height }
        val isHeightVisible = heightSet.size > 1
        product.offers
            .map { offer ->
                OfferItem(
                    offer = offer,
                    isHeightVisible = isHeightVisible,
                    isSelected = offer.barcode == selectedOfferBarcode,
                )
            }
            .toImmutableList()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductAvailabilityInStoresScreenAction.BackClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOfferClicked(offer: OfferItem) {
        selectedOfferBarcode.value = offer.offer.barcode
    }

    private fun getInitiallySelectedOfferBarcode(offers: List<ProductOffer>): Barcode? {
        return if (offers.size > 1) {
            null
        } else {
            offers.firstOrNull()?.barcode
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductAvailabilityInStoresScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Immutable
    data class OfferItem(
        val offer: ProductOffer,
        val isHeightVisible: Boolean,
        val isSelected: Boolean,
    )
}
