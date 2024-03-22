package ru.zarina.zarina.ui.screen.cart

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
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.cart.Cart
import ru.zarina.zarina.domain.cart.CartProduct
import ru.zarina.zarina.domain.cart.CartSize
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.cart.getAvailableCountForDeliveryType
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.base.ErrorState
import ru.zarina.zarina.ui.base.from
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.common.util.ScreenResultHandler
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.navigation.destination.graph.CartGraph
import ru.zarina.zarina.ui.screen.cart.CartViewModel.SideEffect
import ru.zarina.zarina.usecase.cart.GetCartFlowUseCase
import ru.zarina.zarina.usecase.cart.RemoveProductFromCartUseCase
import ru.zarina.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.zarina.zarina.usecase.user.SetUserCityUseCase
import ru.zarina.zarina.util.base.usecase.invoke
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState

@HiltViewModel(assistedFactory = CartViewModel.Factory::class)
class CartViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    savedStateHandle: SavedStateHandle,
    private val interactor: CartInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

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

    private val cartFetchRequests = Channel<Unit>(Channel.CONFLATED)

    private val deliveryTypeToCartResult: StateFlow<Map<DeliveryType, StateFlow<Result<Cart>?>>> =
        combine(
            cartFetchRequests.receiveAsFlow(),
            deliveryTypes,
        ) { _, deliveryTypes ->
            deliveryTypes
                .associateWith { type ->
                    val params = GetCartFlowUseCase.Params(type)
                    interactor.getCartFlow(params)
                        .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(),
                            initialValue = null,
                        )
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = mapOf(),
        )

    val deliveryTypeToCartState: StateFlow<ImmutableMap<DeliveryType, StateFlow<CartState>>> =
        combine(
            flowOf(Unit),
            deliveryTypeToCartResult,
        ) { _, deliveryTypeToCartResult ->
            deliveryTypeToCartResult
                .mapValues { (deliveryType, cartResult) ->
                    cartResult.mapState(
                        scope = viewModelScope,
                        started = SharingStarted.WhileUiSubscribed,
                    ) { result ->
                        result?.fold(
                            onSuccess = { cart ->
                                if (cart.products.isNotEmpty()) {
                                    val productItems = cart.products
                                        .map { product ->
                                            val availableCount =
                                                product.getAvailableCountForDeliveryType(deliveryType)
                                            CartProductItem.Product(
                                                product = product,
                                                availableCount = availableCount,
                                            )
                                        }
                                        .toImmutableList()
                                    CartState.Cart(productItems)
                                } else {
                                    CartState.EmptyCart
                                }
                            },
                            onFailure = { throwable ->
                                val errorState = ErrorState.from(throwable)
                                CartState.Error(errorState)
                            },
                        ) ?: CartState.InitialLoading
                    }
                }
                .toImmutableMap()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = deliveryTypes.value
                .associateWith { MutableStateFlow(CartState.InitialLoading) }
                .toImmutableMap(),
        )

    init {
        cartFetchRequests.trySend(Unit)

        handleCitySelectorResult()
        handleProductCountSelectorResult()
    }

    fun onScreenOpened() {
        cartFetchRequests.trySend(Unit)
    }

    fun onClearCartClicked() {
        viewModelScope.launch {
            interactor.clearCart()
                .onSuccess {
                    cartFetchRequests.trySend(Unit)
                }
                .onFailure {
                    val message = Text.Resource(R.string.cart_clearing_error)
                    emitSideEffect(SideEffect.ShowToast(message))
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
                DeliveryType.PICK_UP_FROM_SHOP -> product.availableCount.pickUpFromShop
            }
            val action = CartScreenAction.ProductCountClicked(
                productId = product.productId,
                barcode = product.barcode,
                initialCount = product.count,
                availableCount = availableCount,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onAddProductToFavoritesClicked(product: CartProduct) {
        viewModelScope.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.productId)
            interactor.toggleProductPresenceInFavorites(params)
                .onSuccess {
                    if (!product.isInFavorites) {
                        val messageText = Text.Resource(R.string.product_adding_to_favorites_completed)
                        val message = ZarinaToastMessage(messageText)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val message = Text.Resource(messageResId)
                    emitSideEffect(SideEffect.ShowToast(message))
                }
        }
    }

    fun onDeleteProductFromCartClicked(product: CartProduct) {
        viewModelScope.launch {
            val params = RemoveProductFromCartUseCase.Params(product.productId, product.barcode)
            interactor.removeProductFromCart(params)
                .onSuccess {
                    cartFetchRequests.trySend(Unit)
                    // TODO: [High] Display temp card that user can use to undo the deletion
                }
                .onFailure {
                    val message = Text.Resource(R.string.product_removing_from_cart_error)
                    emitSideEffect(SideEffect.ShowToast(message))
                }
        }
    }

    fun onGoToCatalogClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.GoToCatalogClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun handleCitySelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.CitySelector.Result>(
                key = UnscopedDestinations.CitySelector.RESULT_KEY,
            ) { result ->
                val newCity = result.city.toCity()
                val currentCity = city.value
                if (newCity.kladrId != currentCity?.kladrId) {
                    val params = SetUserCityUseCase.Params(newCity)
                    interactor.setUserCity(params)
                        .onFailure {
                            val message = Text.Resource(R.string.city_changing_error)
                            emitSideEffect(SideEffect.ShowToast(message))
                        }
                }
            }
        }
    }

    private fun handleProductCountSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<CartGraph.ProductCountSelector.Result>(
                key = CartGraph.ProductCountSelector.RESULT_KEY,
            ) { result ->
                if (result.countChanged) {
                    cartFetchRequests.trySend(Unit)
                }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CartScreenAction) : SideEffect
        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
        data class ShowToast(val message: Text) : SideEffect
    }

    @Stable
    sealed class CartState {
        data object InitialLoading : CartState()

        @Immutable
        data class Cart(
            val productItems: ImmutableList<CartProductItem>,
        ) : CartState()

        data object EmptyCart : CartState()

        @Immutable
        data class Error(val state: ErrorState) : CartState()
    }

    @Stable
    sealed class CartProductItem {
        data class Product(
            val product: CartProduct,
            val availableCount: Int,
        ) : CartProductItem()
    }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): CartViewModel
    }
}
