package ru.zarina.zarina.ui.screen.productcountselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorViewModel.SideEffect
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class ProductCountSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductCountSelectorInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val productId: StateFlow<Product.Id> = savedStateHandle
        .getStateFlow<String?>(
            key = CartGraph.ProductCountSelector.ARG_KEY_PRODUCT_ID,
            initialValue = null,
        )
        .mapState(
            coroutineScope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { string ->
            checkNotNull(string) { "productId is null" }
            Product.Id(string)
        }

    private val barcode: StateFlow<Barcode> = savedStateHandle
        .getStateFlow<String?>(
            key = CartGraph.ProductCountSelector.ARG_KEY_BARCODE,
            initialValue = null,
        )
        .mapState(
            coroutineScope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { string ->
            checkNotNull(string) { "barcode is null" }
            Barcode(string)
        }

    val availbableCount: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CartGraph.ProductCountSelector.ARG_KEY_AVAILABLE_COUNT,
            initialValue = null,
        )
        .mapState(
            coroutineScope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { count ->
            checkNotNull(count) { "availableCount is null" }
            count.coerceAtMost(AVAILABLE_COUNT_MAX_VALUE)
        }

    sealed interface SideEffect : SideEffectSource.SideEffect

    companion object {
        private const val AVAILABLE_COUNT_MAX_VALUE = 10
    }
}
