package ru.zarina.zarina.ui.screen.productcountselector

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorViewModel.SideEffect
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
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
            scope = viewModelScope,
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
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { string ->
            checkNotNull(string) { "barcode is null" }
            Barcode(string)
        }

    private val initialCount: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CartGraph.ProductCountSelector.ARG_KEY_INITIAL_COUNT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { count ->
            checkNotNull(count) { "initialCount is null" }
        }

    private val availableCount: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CartGraph.ProductCountSelector.ARG_KEY_AVAILABLE_COUNT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { count ->
            checkNotNull(count) { "availableCount is null" }
            count.coerceAtMost(AVAILABLE_COUNT_MAX_VALUE)
        }

    private val newCount = MutableStateFlow<Int?>(null)

    private val loadingCountItem = MutableStateFlow<Int?>(null)

    val countItems: StateFlow<ImmutableList<CountItem>> = combine(
        initialCount,
        availableCount,
        newCount,
        loadingCountItem,
    ) { initialCount, availableCount, newCount, loadingCountItem ->
        val selectedCount = newCount ?: initialCount
        List(availableCount) { count ->
            val adjustedCount = count + 1
            CountItem(
                count = adjustedCount,
                isSelected = adjustedCount == selectedCount,
                isLoading = adjustedCount == loadingCountItem,
            )
        }.toImmutableList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = persistentListOf(),
    )

    sealed interface SideEffect : SideEffectSource.SideEffect

    @Immutable
    data class CountItem(
        val count: Int,
        val isSelected: Boolean,
        val isLoading: Boolean,
    )

    companion object {
        private const val AVAILABLE_COUNT_MAX_VALUE = 10
    }
}
