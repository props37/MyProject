package ru.livetyping.zarina.presentation.screen.cart

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartPrice
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartSize
import ru.livetyping.zarina.domain.cart.DeliveryType
import ru.livetyping.zarina.domain.cart.getAvailableCountForDeliveryType
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.presentation.screen.cart.CartViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.usecase.cart.RemoveProductFromCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import ru.livetyping.zarina.domain.cart.Cart as DomainCart

@HiltViewModel(assistedFactory = CartViewModel.Factory::class)
class CartViewModel @AssistedInject constructor(
    @Assisted
    private val citySelectorResultFlow: StateFlow<UnscopedDestinations.CitySelector.Result?>,
    @Assisted
    private val productCountSelectorResultFlow: StateFlow<CartGraph.ProductCountSelector.Result?>,
    savedStateHandle: SavedStateHandle,
    private val interactor: CartInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private var clearCartJob: Job? = null

    val cartSize: StateFlow<CartSize> = interactor.getCartSizeFlow()
        .map { result ->
            result.getOrDefault(CartSize.EMPTY)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = CartSize.EMPTY,
        )

    val city: StateFlow<City?> = interactor.getUserCityFlow()
        .map { result ->
            result.getOrNull() ?: City.DEFAULT
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val isClearCartButtonVisible: StateFlow<Boolean> = cartSize.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { cartSize -> !cartSize.isEmpty }

    val deliveryTypes: StateFlow<ImmutableList<DeliveryType>> =
        MutableStateFlow(DeliveryType.entries.toImmutableList()).asStateFlow()

    private val _currentDeliveryType = MutableStateFlow(DeliveryType.DELIVERY)
    val currentDeliveryType: StateFlow<DeliveryType> = _currentDeliveryType.asStateFlow()

    private val deliveryCartRequester = FlowRequester<Result<DomainCart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(DeliveryType.DELIVERY)
        interactor.getCartFlow(params)
    }

    private val pickUpFromStoreCartRequester = FlowRequester<Result<DomainCart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(DeliveryType.PICK_UP_FROM_STORE)
        interactor.getCartFlow(params)
    }

    private val deliveryCartResult: StateFlow<Result<DomainCart>?> = deliveryCartRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val pickUpFromStoreCartResult: StateFlow<Result<DomainCart>?> =
        pickUpFromStoreCartRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val deliveryCartState: StateFlow<CartState> = combine(
        deliveryCartResult,
        deliveryCartRequester.loadingState,
    ) { result, loadingState ->
        createCartState(result, loadingState, DeliveryType.DELIVERY)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CartState.Loading,
    )

    val pickUpFromStoreCartState = combine(
        pickUpFromStoreCartResult,
        pickUpFromStoreCartRequester.loadingState,
    ) { result, loadingState ->
        createCartState(result, loadingState, DeliveryType.PICK_UP_FROM_STORE)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CartState.Loading,
    )

    init {
        handleCitySelectorResult()
        handleProductCountSelectorResult()
    }

    fun onScreenOpened() {
        deliveryCartRequester.request(CartRequest.LOADING)
        pickUpFromStoreCartRequester.request(CartRequest.LOADING)
        viewModelScope.launch {
            interactor.fetchCartProductIds()
        }
    }

    fun onClearCartClicked() {
        if (clearCartJob?.isActive == true) return

        clearCartJob = viewModelScope.launch {
            interactor.clearCart()
                .onSuccess {
                    deliveryCartRequester.request(CartRequest.LOADING)
                    pickUpFromStoreCartRequester.request(CartRequest.LOADING)
                }
                .onFailure {
                    val text = Text.Resource(R.string.cart_clearing_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onCityClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.CityClicked(city.value)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeliveryTypeChanged(type: DeliveryType) {
        _currentDeliveryType.value = type
    }

    fun onProductCountClicked(product: CartProduct) {
        navigationThrottler.throttle {
            val availableCount = when (currentDeliveryType.value) {
                DeliveryType.DELIVERY -> product.availableCount.delivery
                DeliveryType.PICK_UP_FROM_STORE -> product.availableCount.pickUpFromStore
            }
            val action = CartScreenAction.ProductCountClicked(
                productId = product.productId,
                barcode = product.barcode,
                initialCount = product.count,
                availableCount = availableCount,
                deliveryType = currentDeliveryType.value,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onAddProductToFavoritesClicked(product: CartProduct) {
        viewModelScope.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.productId)
            interactor.toggleProductPresenceInFavorites(params)
                .onSuccess { isProductInFavorites ->
                    if (isProductInFavorites) {
                        val text = Text.Resource(R.string.product_adding_to_favorites_completed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val message = ZarinaToastMessage.error(Text.Resource(messageResId))
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onDeleteProductFromCartClicked(product: CartProduct) {
        viewModelScope.launch {
            val params = RemoveProductFromCartUseCase.Params(product.productId, product.barcode)
            interactor.removeProductFromCart(params)
                .onSuccess {
                    deliveryCartRequester.request(CartRequest.LOADING)
                    pickUpFromStoreCartRequester.request(CartRequest.LOADING)
                }
                .onFailure {
                    val text = Text.Resource(R.string.product_removing_from_cart_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onGoToCatalogClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.GoToCatalogClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCartErrorRefreshClicked() {
        deliveryCartRequester.request(CartRequest.LOADING)
        pickUpFromStoreCartRequester.request(CartRequest.LOADING)
    }

    private fun handleCitySelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.CitySelector.Result>(
                resultFlow = citySelectorResultFlow,
                key = KEY_RESULT_CITY_SELECTOR_RESULT,
            ) { result ->
                val newCity = result.city.toCity()
                val currentCity = city.value
                if (newCity.kladrId != currentCity?.kladrId) {
                    val params = SetUserCityUseCase.Params(newCity)
                    interactor.setUserCity(params)
                        .onFailure {
                            val text = Text.Resource(R.string.city_changing_error)
                            val message = ZarinaToastMessage.error(text)
                            emitSideEffect(SideEffect.ShowZarinaToast(message))
                        }
                }
            }
        }
    }

    private fun handleProductCountSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<CartGraph.ProductCountSelector.Result>(
                resultFlow = productCountSelectorResultFlow,
                key = KEY_RESULT_PRODUCT_COUNT_SELECTOR,
            ) { result ->
                if (result.countChanged) {
                    deliveryCartRequester.request(CartRequest.LOADING)
                    pickUpFromStoreCartRequester.request(CartRequest.LOADING)
                }
            }
        }
    }

    private fun createCartState(
        cartResult: Result<DomainCart>?,
        cartLoadingState: FlowRequester.LoadingState,
        deliveryType: DeliveryType,
    ): CartState {
        return if (cartResult == null || cartLoadingState.isLoading) {
            CartState.Loading
        } else {
            cartResult.fold(
                onSuccess = { cart ->
                    if (cart.products.isNotEmpty()) {
                        val productItems =
                            createCartProductItems(cart, deliveryType).toImmutableList()
                        CartState.Cart(
                            productItems = productItems,
                            price = cart.price,
                        )
                    } else {
                        CartState.EmptyCart
                    }
                },
                onFailure = { throwable ->
                    val errorState = ErrorState.from(throwable)
                    CartState.Error(errorState)
                },
            )
        }
    }

    private fun createCartProductItems(
        cart: Cart,
        deliveryType: DeliveryType,
    ): List<CartProductItem> {
        return cart.products
            .map { product ->
                val availableCount = product.getAvailableCountForDeliveryType(deliveryType)
                CartProductItem.Product(
                    product = product,
                    availableCount = availableCount,
                )
            }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CartScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Stable
    sealed class CartState {
        data object Loading : CartState()

        @Immutable
        data class Cart(
            val productItems: ImmutableList<CartProductItem>,
            val price: CartPrice,
        ) : CartState()

        data object EmptyCart : CartState()

        @Immutable
        data class Error(val state: ErrorState) : CartState()
    }

    @Stable
    sealed class CartProductItem {

        @Immutable
        data class Product(
            val product: CartProduct,
            val availableCount: Int,
        ) : CartProductItem()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            citySelectorResultFlow: StateFlow<UnscopedDestinations.CitySelector.Result?>,
            productCountSelectorResultFlow: StateFlow<CartGraph.ProductCountSelector.Result?>,
        ): CartViewModel
    }

    private enum class CartRequest : FlowRequester.Request { LOADING }

    companion object {
        private const val KEY_RESULT_CITY_SELECTOR_RESULT = "result_city_selector"
        private const val KEY_RESULT_PRODUCT_COUNT_SELECTOR = "result_product_count_selector"
    }
}
