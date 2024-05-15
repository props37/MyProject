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
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.Cart
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
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState

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

    private val cartFetchRequests = Channel<Unit>(Channel.CONFLATED)

    private val deliveryTypeToCartResult: StateFlow<Map<DeliveryType, StateFlow<Result<Cart>?>>> =
        combine(
            cartFetchRequests.receiveAsFlow(),
            deliveryTypes,
        ) { _, deliveryTypes ->
            createDeliveryTypeToCartResult(deliveryTypes)
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
            createDeliveryTypeToCartState(deliveryTypeToCartResult)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = deliveryTypes.value
                .associateWith { MutableStateFlow(CartState.Skeleton) }
                .toImmutableMap(),
        )

    init {
        cartFetchRequests.trySend(Unit)

        handleCitySelectorResult()
        handleProductCountSelectorResult()
    }

    // TODO: [High] Consider refactoring
    fun onScreenOpened() {
        cartFetchRequests.trySend(Unit)
    }

    fun onClearCartClicked() {
        if (clearCartJob?.isActive == true) return

        clearCartJob = viewModelScope.launch {
            interactor.clearCart()
                .onSuccess {
                    cartFetchRequests.trySend(Unit)
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
                    cartFetchRequests.trySend(Unit)
                    // TODO: [High] Display temp card that user can use to undo the deletion
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
                key = CartGraph.ProductCountSelector.RESULT_KEY,
            ) { result ->
                if (result.countChanged) {
                    cartFetchRequests.trySend(Unit)
                }
            }
        }
    }

    private fun createDeliveryTypeToCartResult(
        deliveryTypes: List<DeliveryType>,
    ): Map<DeliveryType, StateFlow<Result<Cart>?>> {
        return deliveryTypes
            .associateWith { type ->
                val params = GetCartFlowUseCase.Params(type)
                interactor.getCartFlow(params)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(),
                        initialValue = null,
                    )
            }
    }

    private fun createDeliveryTypeToCartState(
        deliveryTypeToCartResult: Map<DeliveryType, StateFlow<Result<Cart>?>>,
    ): ImmutableMap<DeliveryType, StateFlow<CartState>> {
        return deliveryTypeToCartResult
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
                    ) ?: CartState.Skeleton
                }
            }
            .toImmutableMap()
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CartScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Stable
    sealed class CartState {
        data object Skeleton : CartState()

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
