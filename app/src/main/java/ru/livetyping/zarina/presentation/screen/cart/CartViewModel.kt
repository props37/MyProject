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
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.cart.getAvailableCountForCartType
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
import ru.livetyping.zarina.usecase.cart.ApplyBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.ApplyMyCardToCartUseCase
import ru.livetyping.zarina.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.usecase.cart.RemoveBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.RemoveMyCardFromCartUseCase
import ru.livetyping.zarina.usecase.cart.RemoveProductFromCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.compose.text.clear
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.combineMore
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
    private var bonusJob: Job? = null
    private var myCardJob: Job? = null
    private var promoCodeJob: Job? = null

    val cartProductCount: StateFlow<Int> = interactor.getCartProductCountFlow()
        .map { result ->
            result.getOrDefault(0)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = 0,
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

    val isClearCartButtonVisible: StateFlow<Boolean> = cartProductCount.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        transform = { it != 0 },
    )

    val cartTypes: StateFlow<ImmutableList<CartType>> =
        MutableStateFlow(CartType.entries.toImmutableList()).asStateFlow()

    private val _currentCartType = MutableStateFlow(CartType.DELIVERY)
    val currentCartType: StateFlow<CartType> = _currentCartType.asStateFlow()

    private val isMyCardApplied = MutableStateFlow(false)

    private val isDeliveryBonusWriteOffApplied = MutableStateFlow(false)
    private val isPickupBonusWriteOffApplied = MutableStateFlow(false)

    private val deliveryCartRequester = FlowRequester<Result<DomainCart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(CartType.DELIVERY)
        interactor.getCartFlow(params)
    }

    private val pickupCartRequester = FlowRequester<Result<DomainCart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(CartType.PICKUP)
        interactor.getCartFlow(params)
    }

    private val deliveryCartResult: StateFlow<Result<DomainCart>?> = deliveryCartRequester.flow
        .onEach { result ->
            val cart = result.getOrNull()
            if (cart != null) {
                updatePromoCodeState(cart)
                updateBonusWriteOffState(cart, CartType.DELIVERY)
                updateMyCardState(cart)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val pickupCartResult: StateFlow<Result<DomainCart>?> = pickupCartRequester.flow
        .onEach { result ->
            val cart = result.getOrNull()
            if (cart != null) {
                updatePromoCodeState(cart)
                updateBonusWriteOffState(cart, CartType.PICKUP)
                updateMyCardState(cart)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val cartSize: StateFlow<CartSize> = deliveryCartResult.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        transform = { result ->
            val cart = result?.getOrNull()
            cart?.size ?: CartSize.EMPTY
        },
    )

    private val isPromoCodeInvalid = MutableStateFlow(false)
    private val promoCodeDescription = MutableStateFlow<Text?>(null)

    val deliveryCartState: StateFlow<CartState> = combineMore(
        deliveryCartResult,
        deliveryCartRequester.loadingState,
        isDeliveryBonusWriteOffApplied,
        isMyCardApplied,
        isPromoCodeInvalid,
        promoCodeDescription,
    ) { result, loadingState, isBonusWriteOffApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        createCartState(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = CartType.DELIVERY,
            isBonusWriteOffApplied = isBonusWriteOffApplied,
            isMyCardApplied = isMyCardApplied,
            isPromoCodeInvalid = isPromoCodeInvalid,
            promoCodeDescription = promoCodeDescription,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CartState.Loading,
    )

    val pickupCartState: StateFlow<CartState> = combineMore(
        pickupCartResult,
        pickupCartRequester.loadingState,
        isPickupBonusWriteOffApplied,
        isMyCardApplied,
        isPromoCodeInvalid,
        promoCodeDescription,
    ) { result, loadingState, isBonusWriteOffApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        createCartState(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = CartType.PICKUP,
            isBonusWriteOffApplied = isBonusWriteOffApplied,
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
        pickupCartRequester.loadingState,
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
    private val promoCodeTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val deliveryBonusWriteOffTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val pickupBonusWriteOffTextFieldState: TextFieldState by savedStateHandle.saveable(
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
        viewModelScope.launch {
            interactor.fetchCartProductIds()
        }
        viewModelScope.launch {
            interactor.fetchUserCity()
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

    fun onCartTypeChanged(type: CartType) {
        _currentCartType.value = type
    }

    fun onProductClicked(product: CartProduct) {
        navigationThrottler.throttle {
            val action = CartScreenAction.ProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onProductCountClicked(product: CartProduct) {
        navigationThrottler.throttle {
            val availableCount = when (currentCartType.value) {
                CartType.DELIVERY -> product.availableCount.delivery
                CartType.PICKUP -> product.availableCount.pickup
            }
            val action = CartScreenAction.ProductCountClicked(
                productId = product.productId,
                barcode = product.barcode,
                initialCount = product.count,
                availableCount = availableCount,
                cartType = currentCartType.value,
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

    fun onIsBonusWriteOffAppliedChanged(isApplied: Boolean) {
        if (bonusJob?.isActive == true) return

        val cartType = currentCartType.value
        val isBonusWriteOffAppliedState = getIsBonusWriteOffAppliedState(cartType)
        isBonusWriteOffAppliedState.value = isApplied
        bonusJob = viewModelScope.launch {
            if (isApplied) {
                val cart = getCart(cartType)
                val maxBonusCountToWriteOff = cart?.bonuses?.writeOff?.max ?: return@launch
                applyBonusWriteOff(cartType, maxBonusCountToWriteOff)
            } else {
                removeBonusWriteOff(cartType)
            }
        }
    }

    fun onBonusCountToWriteOffChanged(bonusCount: Int?) {
        if (bonusJob?.isActive == true) return

        val cartType = currentCartType.value
        val maxBonusCount = getCart(cartType)?.bonuses?.writeOff?.max ?: return
        bonusJob = viewModelScope.launch {
            if (bonusCount != null) {
                applyBonusWriteOff(cartType, bonusCount.coerceAtMost(maxBonusCount))
            } else {
                removeBonusWriteOff(cartType)
            }
        }
    }

    fun onIsMyCardAppliedChanged(isApplied: Boolean) {
        if (myCardJob?.isActive == true) return

        isMyCardApplied.value = isApplied
        myCardJob = viewModelScope.launch {
            val cartType = currentCartType.value
            if (isApplied) {
                val cart = getCart(cartType)
                val productsFirstPriceSum = cart?.myCard?.productsFirstPriceSum ?: return@launch
                applyMyCardToCart(cartType, productsFirstPriceSum)
            } else {
                removeMyCardFromCart(cartType)
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
                    // TODO: [High] Show toasts
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
                        // TODO: [High] Show toasts
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

    fun onPromoCodeImeDoneClicked() {
        if (promoCodeTextFieldState.text.isNotBlank()) {
            onApplyPromoCodeClicked()
        }
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    fun onCheckoutClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.CheckoutClicked(currentCartType.value)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private suspend fun applyBonusWriteOff(cartType: CartType, bonusCount: Int) {
        val params = ApplyBonusWriteOffUseCase.Params(cartType, bonusCount)
        interactor.applyBonusWriteOff(params)
            .onSuccess {
                // TODO: [High] Show toasts
                emitSideEffect(SideEffect.HideKeyboard)
                requestCarts(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.bonus_write_off_applying_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun removeBonusWriteOff(cartType: CartType) {
        val params = RemoveBonusWriteOffUseCase.Params(cartType)
        interactor.removeBonusWriteOff(params)
            .onSuccess {
                // TODO: [High] Show toasts
                emitSideEffect(SideEffect.HideKeyboard)
                requestCarts(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.bonus_write_off_removing_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun applyMyCardToCart(cartType: CartType, productsFirstPriceSum: Int) {
        val cart = getCart(cartType)
        val isBonusWriteOffApplied = getIsBonusWriteOffAppliedState(cartType).value
        val isPromoCodeApplied = cart?.promoCode?.isApplied == true

        val params = ApplyMyCardToCartUseCase.Params(cartType, productsFirstPriceSum)
        interactor.applyMyCardToCart(params)
            .onSuccess {
                requestCarts(CartRequest.REFRESHING)
                showMyCardReplacedOtherBonusToast(
                    isBonusWriteOffApplied = isBonusWriteOffApplied,
                    isPromoCodeApplied = isPromoCodeApplied,
                )
            }
            .onFailure {
                isMyCardApplied.value = false
                val messageText = Text.Resource(R.string.my_card_applying_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun removeMyCardFromCart(cartType: CartType) {
        val params = RemoveMyCardFromCartUseCase.Params(cartType)
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

    private fun showMyCardReplacedOtherBonusToast(
        isBonusWriteOffApplied: Boolean,
        isPromoCodeApplied: Boolean,
    ) {
        val messageText = when {
            isBonusWriteOffApplied -> {
                Text.Resource(R.string.my_card_cant_be_combined_with_bonuses)
            }

            isPromoCodeApplied -> {
                Text.Resource(R.string.my_card_cant_be_combined_with_promo_code)
            }

            else -> return
        }
        val message = ZarinaToastMessage(
            text = messageText,
            duration = ZarinaToastMessage.DURATION_LONG,
        )
        emitSideEffect(SideEffect.ShowZarinaToast(message))
    }

    private fun handleCitySelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.CitySelector.Result>(
                resultFlow = citySelectorResultFlow,
                key = KEY_RESULT_CITY_SELECTOR_RESULT,
            ) { result ->
                val newCity = result.city.toCity()
                val currentCity = city.value
                if (newCity.id != currentCity?.id) {
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
        cartType: CartType,
        isBonusWriteOffApplied: Boolean,
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
                            createProductItems(cart, cartType).toImmutableList()
                        val isBonusWriteOffAvailable =
                            cart.bonuses.available > 0 && cart.myCard?.isApplied != true
                        val bonusState = BonusState(
                            bonuses = cart.bonuses,
                            isWriteOffAvailable = isBonusWriteOffAvailable,
                            isWriteOffApplied = isBonusWriteOffApplied || cart.bonuses.writeOff.isApplied,
                            writeOffTextFieldState = when (cartType) {
                                CartType.DELIVERY -> deliveryBonusWriteOffTextFieldState
                                CartType.PICKUP -> pickupBonusWriteOffTextFieldState
                            },
                        )
                        val myCardState = cart.myCard?.let {
                            MyCardState(
                                isApplied = isMyCardApplied,
                                info = it.info,
                            )
                        }
                        val promoCodeState = if (cart.myCard?.isApplied != true) {
                            PromoCodeState(
                                isApplied = cart.promoCode?.isApplied == true,
                                isInvalid = isPromoCodeInvalid,
                                description = promoCodeDescription,
                                textFieldState = promoCodeTextFieldState,
                            )
                        } else null
                        CartState.Cart(
                            productItems = productItems,
                            price = cart.price,
                            bonusState = bonusState,
                            myCardState = myCardState,
                            promoCodeState = promoCodeState,
                            productLimit = cart.productLimit,
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

    private fun updatePromoCodeState(cart: Cart) {
        promoCodeTextFieldState.edit {
            clear()
            if (cart.promoCode != null) {
                append(cart.promoCode.value)
                placeCursorAtEnd()
            }
        }
    }

    private fun updateBonusWriteOffState(cart: Cart, cartType: CartType) {
        val isBonusWriteOffAppliedState = getIsBonusWriteOffAppliedState(cartType)
        isBonusWriteOffAppliedState.value = cart.bonuses.writeOff.isApplied
        val textFieldState = when (cartType) {
            CartType.DELIVERY -> deliveryBonusWriteOffTextFieldState
            CartType.PICKUP -> pickupBonusWriteOffTextFieldState
        }
        textFieldState.edit {
            clear()
            if (cart.bonuses.writeOff.isApplied) {
                append(cart.bonuses.writeOff.value.toString())
                placeCursorAtEnd()
            }
        }
    }

    private fun updateMyCardState(cart: Cart) {
        isMyCardApplied.value = cart.myCard?.isApplied == true
    }

    private fun createProductItems(
        cart: DomainCart,
        cartType: CartType,
    ): List<ProductItem> {
        return cart.products
            .map { product ->
                val availableCount = product.getAvailableCountForCartType(cartType)
                ProductItem(
                    product = product,
                    availableCount = availableCount,
                )
            }
    }

    private fun requestCarts(request: CartRequest) {
        deliveryCartRequester.request(request)
        pickupCartRequester.request(request)
    }

    private fun getCart(cartType: CartType): Cart? {
        return when (cartType) {
            CartType.DELIVERY -> deliveryCartResult.value
            CartType.PICKUP -> pickupCartResult.value
        }?.getOrNull()
    }

    private fun getIsBonusWriteOffAppliedState(cartType: CartType): MutableStateFlow<Boolean> {
        return when (cartType) {
            CartType.DELIVERY -> isDeliveryBonusWriteOffApplied
            CartType.PICKUP -> isPickupBonusWriteOffApplied
        }
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
            val bonusState: BonusState,
            val myCardState: MyCardState?,
            val promoCodeState: PromoCodeState?,
            val productLimit: DomainCart.ProductLimit,
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

    @Stable
    data class BonusState(
        val bonuses: DomainCart.Bonuses,
        val isWriteOffAvailable: Boolean,
        val isWriteOffApplied: Boolean,
        val writeOffTextFieldState: TextFieldState,
    )

    @Immutable
    data class MyCardState(
        val isApplied: Boolean,
        val info: String?,
    )

    @Stable
    data class PromoCodeState(
        val isApplied: Boolean,
        val isInvalid: Boolean,
        val description: Text?,
        val textFieldState: TextFieldState,
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
