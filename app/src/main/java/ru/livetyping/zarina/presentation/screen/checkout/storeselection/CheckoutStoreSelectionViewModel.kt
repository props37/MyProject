package ru.livetyping.zarina.presentation.screen.checkout.storeselection

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.storeselection.CheckoutStoreSelectionViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.usecase.checkout.GetPickupStoresFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CheckoutStoreSelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutStoreSelectionInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartType = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = CheckoutGraph.StoreSelection.ARG_KEY_CART_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "cartType is null" }
            parcelable.toCartType()
        }

    val step: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CheckoutGraph.StoreSelection.ARG_KEY_STEP,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "step is null" }
        }

    val stepCount: StateFlow<Int> = MutableStateFlow(cartType.value.checkoutStepCount).asStateFlow()

    val city: StateFlow<City?> = interactor.getUserCityFlow()
        .map { it.getOrDefault(City.DEFAULT) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    private val cartRequester = FlowRequester(CartRequest) {
        val params = GetCartFlowUseCase.Params(cartType.value)
        interactor.getCartFlow(params)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val storesRequester = FlowRequester(StoresRequest) {
        city.flatMapLatest {
            val city = it ?: City.DEFAULT
            val params = GetPickupStoresFlowUseCase.Params(city)
            interactor.getPickupStoresFlow(params)
        }
    }

    private val cartResult: StateFlow<Result<Cart>?> = cartRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val storesResult: StateFlow<Result<List<PickupStore>>?> = storesRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val state: StateFlow<State> = combine(
        cartResult,
        storesResult,
        cartRequester.loadingState,
        storesRequester.loadingState,
    ) { cartResult, storesResult, cartLoadingState, storesLoadingState ->
        createState(cartResult, storesResult, cartLoadingState, storesLoadingState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = State.Loading,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutStoreSelectionScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutStoreSelectionScreenAction.CheckoutClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onStoresErrorRefreshClicked() {
        if (cartResult.value?.isSuccess != true) {
            cartRequester.request(CartRequest)
        }
        if (storesResult.value?.isSuccess != true) {
            storesRequester.request(StoresRequest)
        }
    }

    fun onStoreClicked(store: PickupStore) {
        val cart = cartResult.value?.getOrNull() ?: run {
            Timber.v("onStoreClicked skipped because cart is null")
            return
        }
        val availableProducts = cart.products.filter { it.offerId in store.availableItemIds }
        if (availableProducts.isEmpty()) {
            Timber.v("onStoreClicked skipped because there are no available products")
            return
        }

        navigationThrottler.throttle {
            val action = CheckoutStoreSelectionScreenAction.StoreClicked(
                cartType = cartType.value,
                step = step.value,
                store = store.store,
                availableProducts = availableProducts,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun createState(
        cartResult: Result<Cart>?,
        storesResult: Result<List<PickupStore>>?,
        cartLoadingState: FlowRequester.LoadingState,
        storesLoadingState: FlowRequester.LoadingState,
    ): State {
        if (cartResult == null || storesResult == null) return State.Loading
        if (cartLoadingState.isLoading() || storesLoadingState.isLoading()) return State.Loading

        return if (cartResult.isSuccess && storesResult.isSuccess) {
            val cart = cartResult.getOrThrow()
            val stores = storesResult.getOrThrow()
            State.Stores(
                stores = stores,
                cartItemCount = cart.products.size,
            )
        } else {
            val exception = cartResult.exceptionOrNull() ?: storesResult.exceptionOrNull()
            val errorState = exception?.let { ErrorState.from(it) } ?: ErrorState.GENERIC
            State.Error(errorState)
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutStoreSelectionScreenAction) : SideEffect
    }

    @Stable
    sealed class State {
        data object Loading : State()

        @Immutable
        data class Stores(
            val stores: List<PickupStore>,
            val cartItemCount: Int,
        ) : State()

        @Immutable
        data class Error(val state: ErrorState) : State()
    }

    private data object CartRequest : FlowRequester.Request

    private data object StoresRequest : FlowRequester.Request
}
