package ru.livetyping.zarina.presentation.screen.cart

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
import ru.livetyping.zarina.domain.common.Url
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
import ru.livetyping.zarina.usecase.cart.ApplyMyCardToCartUseCase
import ru.livetyping.zarina.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.usecase.cart.RemoveMyCardFromCartUseCase
import ru.livetyping.zarina.usecase.cart.RemoveProductFromCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.clear
import ru.livetyping.zarina.util.compose.text.textAsFlow
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
    private var applyMyCardJob: Job? = null
    private var promoCodeJob: Job? = null

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

    // TODO: [High] Does it affect both carts? Or should we hold separate values for each one?
    private val isMyCardApplied = MutableStateFlow(false)

    private val deliveryCartRequester = FlowRequester<Result<DomainCart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(DeliveryType.DELIVERY)
        interactor.getCartFlow(params)
    }

    private val pickUpFromStoreCartRequester = FlowRequester<Result<DomainCart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(DeliveryType.PICK_UP_FROM_STORE)
        interactor.getCartFlow(params)
    }

    private val deliveryCartResult: StateFlow<Result<DomainCart>?> = deliveryCartRequester.flow
        .onEach { result ->
            val cart = result.getOrNull()
            if (cart != null) updatePromoCode(cart)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val pickUpFromStoreCartResult: StateFlow<Result<DomainCart>?> =
        pickUpFromStoreCartRequester.flow
            .onEach { result ->
                val cart = result.getOrNull()
                if (cart != null) updatePromoCode(cart)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    private val isPromoCodeInvalid = MutableStateFlow(false)
    private val promoCodeDescription = MutableStateFlow<Text?>(null)

    val deliveryCartState: StateFlow<CartState> = combine(
        deliveryCartResult,
        deliveryCartRequester.loadingState,
        isMyCardApplied,
        isPromoCodeInvalid,
        promoCodeDescription,
    ) { result, loadingState, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        createCartState(
            cartResult = result,
            cartLoadingState = loadingState,
            deliveryType = DeliveryType.DELIVERY,
            isMyCardApplied = isMyCardApplied,
            isPromoCodeInvalid = isPromoCodeInvalid,
            promoCodeDescription = promoCodeDescription,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CartState.Loading,
    )

    val pickUpFromStoreCartState: StateFlow<CartState> = combine(
        pickUpFromStoreCartResult,
        pickUpFromStoreCartRequester.loadingState,
        isMyCardApplied,
        isPromoCodeInvalid,
        promoCodeDescription,
    ) { result, loadingState, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        createCartState(
            cartResult = result,
            cartLoadingState = loadingState,
            deliveryType = DeliveryType.PICK_UP_FROM_STORE,
            isMyCardApplied = isMyCardApplied,
            isPromoCodeInvalid = isPromoCodeInvalid,
            promoCodeDescription = promoCodeDescription,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CartState.Loading,
    )

    val isRefreshing: StateFlow<Boolean> = combine(
        deliveryCartRequester.loadingState,
        pickUpFromStoreCartRequester.loadingState,
    ) { deliveryLoadingState, pickUpLoadingState ->
        val isDeliveryCartRefreshing = deliveryLoadingState.loadingRequest == CartRequest.REFRESHING
        val isPickUpCartRefreshing = pickUpLoadingState.loadingRequest == CartRequest.REFRESHING
        isDeliveryCartRefreshing || isPickUpCartRefreshing
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = false,
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    val promoCodeTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    init {
        resetPromoCodeErrorsOnChange()
        handleCitySelectorResult()
        handleProductCountSelectorResult()
    }

    fun onScreenOpened() {
        requestCarts(CartRequest.LOADING)
        // TODO: [High] Is it needed?
        viewModelScope.launch {
            interactor.fetchCartProductIds()
        }
    }

    fun onClearCartClicked() {
        if (clearCartJob?.isActive == true) return

        clearCartJob = viewModelScope.launch {
            interactor.clearCart()
                .onSuccess {
                    requestCarts(CartRequest.REFRESHING)
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
                    requestCarts(CartRequest.REFRESHING)
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
        requestCarts(CartRequest.LOADING)
    }

    fun onIsMyCardAppliedChanged(isApplied: Boolean) {
        if (applyMyCardJob?.isActive == true) return

        isMyCardApplied.value = isApplied
        applyMyCardJob = viewModelScope.launch {
            val deliveryType = currentDeliveryType.value
            if (isApplied) {
                val cart = when (deliveryType) {
                    DeliveryType.DELIVERY -> deliveryCartResult.value
                    DeliveryType.PICK_UP_FROM_STORE -> pickUpFromStoreCartResult.value
                }?.getOrNull()
                val productsFirstPriceSum = cart?.myCard?.productsFirstPriceSum ?: return@launch
                applyMyCardToCart(deliveryType, productsFirstPriceSum)
            } else {
                removeMyCardFromCart(deliveryType)
            }
        }
    }

    fun onApplyPromoCodeClicked() {
        if (promoCodeJob?.isActive == true) return

        promoCodeJob = viewModelScope.launch {
            val promoCode = promoCodeTextFieldState.text.toString()
            val params = ApplyPromoCodeUseCase.Params(promoCode)
            interactor.applyPromoCode(params)
                .onSuccess {
                    // TODO: [High] Show toasts if needed
                    emitSideEffect(SideEffect.HideKeyboard)
                    requestCarts(CartRequest.REFRESHING)
                }
                .onFailure {
                    val messageText = Text.Resource(R.string.promo_code_applying_error)
                    val message = ZarinaToastMessage.error(messageText)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))

                    // TODO: [High] Do only if PromoCodeNotFoundException is caught
                    isPromoCodeInvalid.value = true
                    promoCodeDescription.value =
                        Text.Resource(R.string.promo_code_applying_error_description)
                }
        }
    }

    fun onRemovePromoCodeClicked() {
        if (promoCodeJob?.isActive == true) return

        val isPromoCodeApplied =
            (deliveryCartState.value as? CartState.Cart)?.promoCodeState?.isApplied == true
        if (isPromoCodeApplied) {
            emitSideEffect(SideEffect.HideKeyboard)
            promoCodeJob = viewModelScope.launch {
                interactor.removePromoCode()
                    .onSuccess {
                        // TODO: [High] Show toasts if needed
                        requestCarts(CartRequest.REFRESHING)
                    }
                    .onFailure {
                        val messageText = Text.Resource(R.string.promo_code_removing_error)
                        val message = ZarinaToastMessage.error(messageText)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                    }
            }
        } else {
            promoCodeTextFieldState.clearText()
        }
    }

    // TODO: [High] Handle promo code TF IME action

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    private suspend fun applyMyCardToCart(deliveryType: DeliveryType, productsFirstPriceSum: Int) {
        val params = ApplyMyCardToCartUseCase.Params(deliveryType, productsFirstPriceSum)
        interactor.applyMyCardToCart(params)
            .onSuccess {
                // TODO: [High] Show toast if MyCard replaces bonuses
                // TODO: [High] Show toast if MyCard replaces promo code
                requestCarts(CartRequest.REFRESHING)
            }
            .onFailure {
                isMyCardApplied.value = false
                val messageText = Text.Resource(R.string.my_card_applying_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun removeMyCardFromCart(deliveryType: DeliveryType) {
        val params = RemoveMyCardFromCartUseCase.Params(deliveryType)
        interactor.removeMyCardFromCart(params)
            .onSuccess {
                requestCarts(CartRequest.REFRESHING)
            }
            .onFailure {
                isMyCardApplied.value = true
                val messageText = Text.Resource(R.string.my_card_canceling_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private fun resetPromoCodeErrorsOnChange() {
        promoCodeTextFieldState.textAsFlow()
            .onEach {
                isPromoCodeInvalid.value = false
                promoCodeDescription.value = null
            }
            .launchIn(viewModelScope)
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
                    requestCarts(CartRequest.REFRESHING)
                }
            }
        }
    }

    private fun createCartState(
        cartResult: Result<DomainCart>?,
        cartLoadingState: FlowRequester.LoadingState,
        deliveryType: DeliveryType,
        isMyCardApplied: Boolean,
        isPromoCodeInvalid: Boolean,
        promoCodeDescription: Text?,
    ): CartState {
        val isLoading = cartLoadingState is FlowRequester.LoadingState.Loading
                && cartLoadingState.request == CartRequest.LOADING
        return if (cartResult == null || isLoading) {
            CartState.Loading
        } else {
            cartResult.fold(
                onSuccess = { cart ->
                    if (cart.products.isNotEmpty()) {
                        val productItems =
                            createProductItems(cart, deliveryType).toImmutableList()
                        val myCardState = cart.myCard?.let {
                            MyCardState(
                                // TODO: [High] Set based on backend model?
                                isApplied = isMyCardApplied || it.isApplied,
                                info = it.info,
                            )
                        }
                        val promoCodeState = PromoCodeState(
                            isApplied = cart.promoCode?.isApplied == true,
                            isInvalid = isPromoCodeInvalid,
                            description = promoCodeDescription,
                        )
                        CartState.Cart(
                            productItems = productItems,
                            price = cart.price,
                            bonuses = cart.bonuses,
                            myCardState = myCardState,
                            promoCodeState = promoCodeState,
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

    private fun updatePromoCode(cart: Cart) {
        promoCodeTextFieldState.edit {
            clear()
            if (cart.promoCode != null) {
                append(cart.promoCode.value)
                placeCursorAtEnd()
            }
        }
    }

    private fun createProductItems(
        cart: DomainCart,
        deliveryType: DeliveryType,
    ): List<ProductItem> {
        return cart.products
            .map { product ->
                val availableCount = product.getAvailableCountForDeliveryType(deliveryType)
                ProductItem(
                    product = product,
                    availableCount = availableCount,
                )
            }
    }

    private fun requestCarts(request: CartRequest) {
        deliveryCartRequester.request(request)
        pickUpFromStoreCartRequester.request(request)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CartScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect

        data object HideKeyboard : SideEffect
    }

    @Stable
    sealed class CartState {
        data object Loading : CartState()

        @Immutable
        data class Cart(
            val productItems: ImmutableList<ProductItem>,
            val price: CartPrice,
            val bonuses: DomainCart.Bonuses,
            val myCardState: MyCardState?,
            val promoCodeState: PromoCodeState,
        ) : CartState()

        data object EmptyCart : CartState()

        @Immutable
        data class Error(val state: ErrorState) : CartState()
    }

    @Immutable
    data class ProductItem(
        val product: CartProduct,
        val availableCount: Int,
    )

    @Immutable
    data class MyCardState(val isApplied: Boolean, val info: String?)

    @Immutable
    data class PromoCodeState(
        val isApplied: Boolean,
        val isInvalid: Boolean,
        val description: Text?,
    )

    @AssistedFactory
    interface Factory {
        fun create(
            citySelectorResultFlow: StateFlow<UnscopedDestinations.CitySelector.Result?>,
            productCountSelectorResultFlow: StateFlow<CartGraph.ProductCountSelector.Result?>,
        ): CartViewModel
    }

    private enum class CartRequest : FlowRequester.Request { LOADING, REFRESHING }

    companion object {
        private const val KEY_RESULT_CITY_SELECTOR_RESULT = "result_city_selector"
        private const val KEY_RESULT_PRODUCT_COUNT_SELECTOR = "result_product_count_selector"
    }
}
