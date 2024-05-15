package ru.livetyping.zarina.presentation.screen.productcountselector

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.DeliveryType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.model.cart.DeliveryTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.presentation.screen.productcountselector.ProductCountSelectorViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.ChangeProductCountInCartUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductCountSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductCountSelectorInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var changeProductCountInCartJob: Job? = null

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

    private val deliveryType: StateFlow<DeliveryType> = savedStateHandle
        .getStateFlow<DeliveryTypeParcelable?>(
            key = CartGraph.ProductCountSelector.ARG_KEY_DELIVERY_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "deliveryType is null" }
            parcelable.toDeliveryType()
        }

    private val currentCount = MutableStateFlow(initialCount.value)

    private val loadingCountItem = MutableStateFlow<Int?>(null)

    val countItems: StateFlow<ImmutableList<CountItem>> = combine(
        availableCount,
        currentCount,
        loadingCountItem,
    ) { availableCount, currentCount, loadingCountItem ->
        val selectedCount = currentCount
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

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = ProductCountSelectorScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCountItemClicked(item: CountItem) {
        changeProductCountInCartJob?.cancel()

        val count = item.count
        if (count == currentCount.value) {
            val action = ProductCountSelectorScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
            return
        }

        loadingCountItem.value = count
        changeProductCountInCartJob = viewModelScope.launch {
            val params = ChangeProductCountInCartUseCase.Params(
                productId = productId.value,
                barcode = barcode.value,
                count = count,
                deliveryType = deliveryType.value,
            )
            interactor.changeProductCountInCart(params)
                .onSuccess {
                    currentCount.value = count
                    val action = ProductCountSelectorScreenAction.CountChanged
                    emitSideEffect(SideEffect.Navigate(action))
                }
                .onFailure { throwable ->
                    when (throwable) {
                        is CancellationException -> return@onFailure
                        is ClientRequestException -> {
                            val text = Text.Resource(
                                R.string.product_changing_count_in_cart_count_not_enough_product_error,
                            )
                            val message = ZarinaToastMessage(text)
                            emitSideEffect(SideEffect.ShowZarinaToast(message))
                        }

                        else -> {
                            val text = Text.Resource(R.string.product_changing_count_in_cart_error)
                            val message = ZarinaToastMessage.error(text)
                            emitSideEffect(SideEffect.ShowZarinaToast(message))
                        }
                    }
                }
            ensureActive()
            loadingCountItem.value = null
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductCountSelectorScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

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
