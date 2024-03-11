package ru.zarina.zarina.ui.screen.sizeselector

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductOffer
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorViewModel.SideEffect
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class SizeSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val product: StateFlow<Product> = savedStateHandle
        .getStateFlow<ProductParcelable?>(
            key = SizeSelectorGraph.SizeSelector.ARG_KEY_PRODUCT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "product is null" }
            parcelable.toProduct()
        }

    val sizes: StateFlow<ImmutableList<Size>> = product.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
    ) { product ->
        val items = mutableListOf<Size>()
        product.offers
            .groupBy {
                if (it.sizeRu != null) "${it.size} ${it.sizeRu}" else it.size
            }
            .forEach { (size, offers) ->
                val item = Size(size = size, offers = offers.toImmutableList())
                items.add(item)
            }
        items.toImmutableList()
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = SizeSelectorScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSizeClicked(size: Size) {
        // TODO: [High] Add product to cart if it is available
        navigationThrottler.throttle {
            val action = SizeSelectorScreenAction.SizeClicked(product.value, size.offers)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SizeSelectorScreenAction) : SideEffect
    }

    @Immutable
    data class Size(
        val size: String,
        val offers: ImmutableList<ProductOffer>,
    ) {
        val id: String get() = size

        val isAvailable: Boolean = offers.any { it.isAvailable }

        val availableHeights: ImmutableList<String> = offers
            .filter { it.isAvailable }
            .mapNotNull { it.height }
            .sorted()
            .toImmutableList()
    }
}
