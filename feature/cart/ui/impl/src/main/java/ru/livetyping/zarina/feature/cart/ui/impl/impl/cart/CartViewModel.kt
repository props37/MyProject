package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.cart.CartSize
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.usecase.cart.ApplyMyCardUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.CancelBonusRedemptionUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.ChangeProductCountInCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.RedeemBonusesUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.RemoveProductFromCartUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.WithdrawMyCardUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase
import ru.livetyping.zarina.core.navigationutil.ScreenResultHandler
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.feature.cart.ui.api.CartSelectedCityResult
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartRequest
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartStateBuilder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector.ProductCountItem
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector.ProductCountSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector.ProductCountSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel.CartBonusAccountStateHolder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel.CartMyCardStateHolder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel.CartPromoCodeStateHolder
import ru.livetyping.zarina.core.resource.R as RCommon

// TODO: [Top] Handle city change

@HiltViewModel(assistedFactory = CartViewModel.Factory::class)
internal class CartViewModel @AssistedInject constructor(
    @Assisted
    selectedCityResultFlow: Flow<CartSelectedCityResult?>,
    savedStateHandle: SavedStateHandle,
    private val deps: CartDeps,
) : ViewModel(), SideEffectSource<CartSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val cartStateBuilder = CartStateBuilder()

    private var clearCartJob: Job? = null
    private var bonusAccountJob: Job? = null
    private var myCardJob: Job? = null
    private var promoCodeJob: Job? = null
    private var changeProductCountJob: Job? = null

    val cartProductCount: StateFlow<Int> = deps.getCartProductCountFlow()
        .map { result ->
            result.getOrDefault(0)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = 0,
        )

    private val getUserCityUseCaseParams = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
    val city: StateFlow<City?> = deps.getUserCityFlow(getUserCityUseCaseParams)
        .map { result ->
            result.getOrNull() ?: City.getDefault()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = null,
        )

    val isClearCartButtonVisible: StateFlow<Boolean> = cartProductCount.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        transform = { it != 0 },
    )

    val cartTypes: StateFlow<ImmutableList<CartType>> =
        ReadOnlyStateFlow(CartType.entries.toImmutableList())

    private val _currentCartType = MutableStateFlow(CartType.DELIVERY)
    val currentCartType: StateFlow<CartType> = _currentCartType.asStateFlow()

    private val deliveryBonusAccountStateHolder = CartBonusAccountStateHolder(savedStateHandle)
    private val pickupBonusAccountStateHolder = CartBonusAccountStateHolder(savedStateHandle)

    private val myCardStateHolder = CartMyCardStateHolder()

    private val promoCodeStateHolder = CartPromoCodeStateHolder(savedStateHandle)

    private val deliveryCartRequester = FlowRequester<Result<Cart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(CartType.DELIVERY)
        deps.getCartFlow(params)
    }

    private val pickupCartRequester = FlowRequester<Result<Cart>, CartRequest> {
        val params = GetCartFlowUseCase.Params(CartType.PICKUP)
        deps.getCartFlow(params)
    }

    private val deliveryCartResult: StateFlow<Result<Cart>?> = deliveryCartRequester.flow
        .onEach { result ->
            val cart = result.getOrNull()
            if (cart != null) {
                deliveryBonusAccountStateHolder.updateFromCart(cart)
                myCardStateHolder.updateFromCart(cart)
                promoCodeStateHolder.updateFromCart(cart)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val pickupCartResult: StateFlow<Result<Cart>?> = pickupCartRequester.flow
        .onEach { result ->
            val cart = result.getOrNull()
            if (cart != null) {
                pickupBonusAccountStateHolder.updateFromCart(cart)
                myCardStateHolder.updateFromCart(cart)
                promoCodeStateHolder.updateFromCart(cart)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val cartSize: StateFlow<CartSize> = combine(
        deliveryCartResult,
        pickupCartResult,
    ) { deliveryCartResult, pickupCartResult ->
        val deliveryCart = deliveryCartResult?.getOrNull()
        val pickupCart = pickupCartResult?.getOrNull()
        deliveryCart?.size ?: pickupCart?.size ?: CartSize.getEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CartSize.getEmpty(),
    )

    val deliveryCartState: StateFlow<CartState> = combineMore(
        deliveryCartResult,
        deliveryCartRequester.loadingState,
        deliveryBonusAccountStateHolder.isBonusRedemptionApplied,
        myCardStateHolder.isMyCardApplied,
        promoCodeStateHolder.isPromoCodeInvalid,
        promoCodeStateHolder.promoCodeDescription,
    ) { result, loadingState, isBonusRedemptionApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        cartStateBuilder.build(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = CartType.DELIVERY,
            isBonusRedemptionApplied = isBonusRedemptionApplied,
            bonusRedemptionTextFieldState = deliveryBonusAccountStateHolder.bonusRedemptionTextFieldState,
            isMyCardApplied = isMyCardApplied,
            promoCodeTextFieldState = promoCodeStateHolder.promoCodeTextFieldState,
            isPromoCodeInvalid = isPromoCodeInvalid,
            promoCodeDescription = promoCodeDescription,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CartState.Loading,
    )

    val pickupCartState: StateFlow<CartState> = combineMore(
        pickupCartResult,
        pickupCartRequester.loadingState,
        pickupBonusAccountStateHolder.isBonusRedemptionApplied,
        myCardStateHolder.isMyCardApplied,
        promoCodeStateHolder.isPromoCodeInvalid,
        promoCodeStateHolder.promoCodeDescription,
    ) { result, loadingState, isBonusRedemptionApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        cartStateBuilder.build(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = CartType.PICKUP,
            isBonusRedemptionApplied = isBonusRedemptionApplied,
            bonusRedemptionTextFieldState = pickupBonusAccountStateHolder.bonusRedemptionTextFieldState,
            isMyCardApplied = isMyCardApplied,
            promoCodeTextFieldState = promoCodeStateHolder.promoCodeTextFieldState,
            isPromoCodeInvalid = isPromoCodeInvalid,
            promoCodeDescription = promoCodeDescription,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
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
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = false,
    )

    val isPullRefreshing: StateFlow<Boolean> = combine(
        deliveryCartRequester.loadingState,
        pickupCartRequester.loadingState,
    ) { deliveryLoadingState, pickUpLoadingState ->
        val isDeliveryCartRefreshing =
            deliveryLoadingState.loadingRequest == CartRequest.PULL_REFRESHING
        val isPickUpCartRefreshing =
            pickUpLoadingState.loadingRequest == CartRequest.PULL_REFRESHING
        isDeliveryCartRefreshing && isPickUpCartRefreshing
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = false,
    )

    private val _productCountSelectorState =
        MutableStateFlow<ProductCountSelectorState>(ProductCountSelectorState.None)
    val productCountSelectorState = _productCountSelectorState.asStateFlow()

    init {
        removePromoCodeErrorsOnChange()
        handleSelectedCityResult(selectedCityResultFlow)
    }

    fun onScreenCreated() {
        requestCarts(CartRequest.LOADING)
        viewModelScope.launch {
            val params = GetCartProductIdsFlowUseCase.Params(CachePolicy.Remote())
            deps.getCartProductIdsFlow(params).firstOrNull()
        }
    }

    fun onClearCartClicked() {
        if (clearCartJob?.isActive == true) return

        clearCartJob = viewModelScope.launch {
            deps.clearCart()
                .onSuccess {
                    requestCarts(CartRequest.REFRESHING)
                }
                .onFailure {
                    val text = Text.Resource(R.string.cart_clearing_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(CartSideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.BackClicked
            emitSideEffect(CartSideEffect.Navigate(action))
        }
    }

    fun onCityClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.ChangeCityClicked(city.value)
            emitSideEffect(CartSideEffect.Navigate(action))
        }
    }

    fun onCartTypeChanged(type: CartType) {
        _currentCartType.value = type
    }

    fun onProductClicked(product: CartProduct) {
        navigationThrottler.throttle {
            val action = CartScreenAction.ProductClicked(product)
            emitSideEffect(CartSideEffect.Navigate(action))
        }
    }

    fun onProductCountClicked(product: CartProduct) {
        emitSideEffect(CartSideEffect.HideKeyboard)

        val availableCount = when (currentCartType.value) {
            CartType.DELIVERY -> product.availableCount.delivery
            CartType.PICKUP -> product.availableCount.pickup
        }.coerceAtMost(PRODUCT_COUNT_MAX_VALUE)
        val countItems = List(availableCount) { index ->
            val count = index + 1
            ProductCountItem(
                count = count,
                isSelected = count == product.count,
                isLoading = false,
            )
        }.toImmutableList()
        _productCountSelectorState.value = ProductCountSelectorState.ProductCountSelector(
            product = product,
            countItems = countItems,
        )
    }

    fun onAddProductToWishlistClicked(product: CartProduct) {
        viewModelScope.launch {
            val params = ToggleProductInWishlistUseCase.Params(product.productId)
            deps.toggleProductInWishlist(params)
                .onSuccess { isProductInFavorites ->
                    if (isProductInFavorites) {
                        val text = Text.Resource(RCommon.string.res_product_added_to_wishlist)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(CartSideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInWishlist) {
                        RCommon.string.res_product_removing_from_wishlist_error
                    } else {
                        RCommon.string.res_product_adding_to_wishlist_error
                    }
                    val message = ZarinaToastMessage.error(Text.Resource(messageResId))
                    emitSideEffect(CartSideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onDeleteProductFromCartClicked(product: CartProduct) {
        viewModelScope.launch {
            val params = RemoveProductFromCartUseCase.Params(product.productId, product.barcode)
            deps.removeProductFromCart(params)
                .onSuccess {
                    requestCarts(CartRequest.REFRESHING)
                }
                .onFailure {
                    val text = Text.Resource(RCommon.string.res_product_removing_from_cart_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(CartSideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onGoToCatalogClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.GoToCatalogClicked
            emitSideEffect(CartSideEffect.Navigate(action))
        }
    }

    fun onCartErrorRefreshClicked() {
        if (deliveryCartResult.value?.isFailure == true) {
            requestDeliveryCart(CartRequest.LOADING)
        }
        if (pickupCartResult.value?.isFailure == true) {
            requestPickupCart(CartRequest.LOADING)
        }
    }

    fun onIsBonusRedemptionAppliedChanged(isApplied: Boolean) {
        if (bonusAccountJob?.isActive == true) return

        val cartType = currentCartType.value
        val bonusStateHolder = getBonusStateHolder(cartType)
        bonusStateHolder.setIsBonusRedemptionApplied(isApplied)
        bonusAccountJob = viewModelScope.launch {
            if (isApplied) {
                val cart = getCart(cartType)
                val maxBonusCountToRedeem = cart?.bonusAccount?.redemption?.max ?: return@launch
                redeemBonuses(cartType, maxBonusCountToRedeem)
            } else {
                cancelBonusRedemption(cartType)
            }
        }
    }

    fun onBonusCountToRedeemChanged(bonusCount: Int?) {
        if (bonusAccountJob?.isActive == true) return

        val cartType = currentCartType.value
        val maxBonusCount = getCart(cartType)?.bonusAccount?.redemption?.max ?: return
        bonusAccountJob = viewModelScope.launch {
            if (bonusCount != null) {
                redeemBonuses(cartType, bonusCount.coerceAtMost(maxBonusCount))
            } else {
                cancelBonusRedemption(cartType)
            }
        }
    }

    fun onIsMyCardAppliedChanged(isApplied: Boolean) {
        if (myCardJob?.isActive == true) return

        myCardStateHolder.setIsMyCardApplied(isApplied)
        myCardJob = viewModelScope.launch {
            val cartType = currentCartType.value
            if (isApplied) {
                val cart = getCart(cartType)
                val productsFirstPriceSum = cart?.myCard?.productsFirstPriceSum ?: return@launch
                applyMyCard(cartType, productsFirstPriceSum)
            } else {
                withdrawMyCard(cartType)
            }
        }
    }

    fun onApplyPromoCodeClicked() {
        if (promoCodeJob?.isActive == true) return

        promoCodeJob = viewModelScope.launch {
            val promoCode = promoCodeStateHolder.promoCode
            val params = ApplyPromoCodeUseCase.Params(promoCode)
            deps.applyPromoCode(params)
                .onSuccess {
                    emitSideEffect(CartSideEffect.HideKeyboard)
                    requestCarts(CartRequest.REFRESHING)
                }
                .onFailure {
                    val messageText = Text.Resource(R.string.cart_promo_code_applying_error)
                    val message = ZarinaToastMessage.error(messageText)
                    emitSideEffect(CartSideEffect.ShowZarinaToast(message))

                    // TODO: [High] Do only if PromoCodeNotFoundException is caught
                    promoCodeStateHolder.setIsPromoCodeInvalid(true)
                    val promoCodeDescription = Text.Resource(R.string.cart_promo_code_applying_error_description)
                    promoCodeStateHolder.setPromoCodeDescription(promoCodeDescription)
                }
        }
    }

    fun onWithdrawPromoCodeClicked() {
        if (promoCodeJob?.isActive == true) return

        val isPromoCodeApplied =
            (deliveryCartState.value as? CartState.Cart)?.promoCodeState?.isApplied == true
        if (isPromoCodeApplied) {
            emitSideEffect(CartSideEffect.HideKeyboard)
            promoCodeJob = viewModelScope.launch {
                deps.withdrawPromoCode()
                    .onSuccess {
                        requestCarts(CartRequest.REFRESHING)
                    }
                    .onFailure {
                        val messageText = Text.Resource(R.string.cart_promo_code_withdraw_error)
                        val message = ZarinaToastMessage.error(messageText)
                        emitSideEffect(CartSideEffect.ShowZarinaToast(message))
                    }
            }
        } else {
            promoCodeStateHolder.clearPromoCode()
        }
    }

    fun onPromoCodeImeDoneClicked() {
        if (promoCodeStateHolder.promoCode.isNotBlank()) {
            onApplyPromoCodeClicked()
        }
    }

    fun onCheckoutClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.CheckoutClicked(currentCartType.value)
            emitSideEffect(CartSideEffect.Navigate(action))
        }
    }

    fun onPullRefreshTriggered() {
        if (!deliveryCartRequester.loadingState.value.isLoading()) {
            requestDeliveryCart(CartRequest.PULL_REFRESHING)
        }
        if (!pickupCartRequester.loadingState.value.isLoading()) {
            requestPickupCart(CartRequest.PULL_REFRESHING)
        }
    }

    fun onProductCountSelectorEvent(event: ProductCountSelectorEvent) {
        when (event) {
            ProductCountSelectorEvent.DismissRequested -> {
                changeProductCountJob?.cancel()
                _productCountSelectorState.value = ProductCountSelectorState.None
            }

            is ProductCountSelectorEvent.CountItemClicked -> {
                changeProductCount(
                    product = event.product,
                    count = event.countItem.count,
                    cartType = currentCartType.value,
                )
            }
        }
    }

    private suspend fun redeemBonuses(cartType: CartType, bonusCount: Int) {
        if (bonusCount == 0) {
            val text = Text.Resource(R.string.cart_you_cant_redeem_bonuses_for_this_order)
            val message = ZarinaToastMessage(text = text, duration = ZarinaToastMessage.DURATION_LONG)
            emitSideEffect(CartSideEffect.ShowZarinaToast(message))
        }

        val params = RedeemBonusesUseCase.Params(cartType, bonusCount)
        deps.redeemBonuses(params)
            .onSuccess {
                emitSideEffect(CartSideEffect.HideKeyboard)
                requestCarts(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.cart_bonus_redemption_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(CartSideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun cancelBonusRedemption(cartType: CartType) {
        val params = CancelBonusRedemptionUseCase.Params(cartType)
        deps.cancelBonusRedemption(params)
            .onSuccess {
                emitSideEffect(CartSideEffect.HideKeyboard)
                requestCarts(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.cart_bonus_redemption_canceling_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(CartSideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun applyMyCard(cartType: CartType, productsFirstPriceSum: Int) {
        val cart = getCart(cartType)
        val bonusStateHolder = getBonusStateHolder(cartType)
        val isBonusRedemptionApplied = bonusStateHolder.isBonusRedemptionApplied.value
        val isPromoCodeApplied = cart?.promoCode?.isApplied == true

        val params = ApplyMyCardUseCase.Params(cartType, productsFirstPriceSum)
        deps.applyMyCard(params)
            .onSuccess {
                requestCarts(CartRequest.REFRESHING)
                showMyCardReplacedOtherBonusToast(
                    isBonusRedemptionApplied = isBonusRedemptionApplied,
                    isPromoCodeApplied = isPromoCodeApplied,
                )
            }
            .onFailure {
                myCardStateHolder.setIsMyCardApplied(false)
                val messageText = Text.Resource(R.string.cart_my_card_applying_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(CartSideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun withdrawMyCard(cartType: CartType) {
        val params = WithdrawMyCardUseCase.Params(cartType)
        deps.withdrawMyCard(params)
            .onSuccess {
                requestCarts(CartRequest.REFRESHING)
            }
            .onFailure {
                myCardStateHolder.setIsMyCardApplied(false)
                val messageText = Text.Resource(R.string.cart_my_card_withdraw_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(CartSideEffect.ShowZarinaToast(message))
            }
    }

    private fun removePromoCodeErrorsOnChange() {
        promoCodeStateHolder.promoCodeAsFlow
            .onEach {
                promoCodeStateHolder.setIsPromoCodeInvalid(false)
                promoCodeStateHolder.setPromoCodeDescription(null)
            }
            .launchIn(viewModelScope)
    }

    private fun showMyCardReplacedOtherBonusToast(
        isBonusRedemptionApplied: Boolean,
        isPromoCodeApplied: Boolean,
    ) {
        val messageText = when {
            isBonusRedemptionApplied -> {
                Text.Resource(R.string.cart_my_card_cant_be_combined_with_bonuses)
            }

            isPromoCodeApplied -> {
                Text.Resource(R.string.cart_my_card_cant_be_combined_with_promo_code)
            }

            else -> return
        }
        val message = ZarinaToastMessage(
            text = messageText,
            duration = ZarinaToastMessage.DURATION_LONG,
        )
        emitSideEffect(CartSideEffect.ShowZarinaToast(message))
    }

    private fun changeProductCount(product: CartProduct, count: Int, cartType: CartType) {
        changeProductCountJob?.cancel()

        if (count == product.count) {
            _productCountSelectorState.value = ProductCountSelectorState.None
            return
        }

        changeProductCountJob = viewModelScope.launch {
            try {
                markProductCountItemAsLoading(count)
                val params = ChangeProductCountInCartUseCase.Params(
                    barcode = product.barcode,
                    count = count,
                    cartType = cartType,
                )
                deps.changeProductCount(params)
                    .onSuccess {
                        _productCountSelectorState.value = ProductCountSelectorState.None
                        requestCarts(CartRequest.REFRESHING)
                    }
                    .onFailure(::handleProductCountChangingException)
            } finally {
                markProductCountItemAsNotLoading(count)
            }
        }
    }

    private fun markProductCountItemAsLoading(count: Int) {
        _productCountSelectorState.update { state ->
            when (state) {
                is ProductCountSelectorState.ProductCountSelector -> {
                    val newCountItems = state.countItems.map { item ->
                        if (item.count == count) item.copy(isLoading = true) else item
                    }.toImmutableList()
                    state.copy(countItems = newCountItems)
                }

                ProductCountSelectorState.None -> state
            }
        }
    }

    private fun markProductCountItemAsNotLoading(count: Int) {
        _productCountSelectorState.update { state ->
            when (state) {
                is ProductCountSelectorState.ProductCountSelector -> {
                    val newCountItems = state.countItems.map { item ->
                        if (item.count == count) item.copy(isLoading = false) else item
                    }.toImmutableList()
                    state.copy(countItems = newCountItems)
                }

                ProductCountSelectorState.None -> state
            }
        }
    }

    private fun handleProductCountChangingException(t: Throwable) {
        val text = Text.Resource(R.string.cart_product_count_changing_error)
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(CartSideEffect.ShowZarinaToast(message))
    }

    private suspend fun updateUserCity(city: City) {
        val params = SetUserCityUseCase.Params(city)
        deps.setUserCity(params)
            .onFailure {
                val text = Text.Resource(RCommon.string.res_city_changing_error)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(CartSideEffect.ShowZarinaToast(message))
            }
    }

    private fun handleSelectedCityResult(resultFlow: Flow<CartSelectedCityResult?>) {
        viewModelScope.launch {
            screenResultHandler.handle(
                resultFlow = resultFlow,
                key = Keys.SELECTED_CITY_RESULT.key,
            ) { result ->
                updateUserCity(result.city)
            }
        }
    }

    private fun requestCarts(request: CartRequest) {
        requestDeliveryCart(request)
        requestPickupCart(request)
    }

    private fun requestDeliveryCart(request: CartRequest) {
        deliveryCartRequester.request(request)
    }

    private fun requestPickupCart(request: CartRequest) {
        pickupCartRequester.request(request)
    }

    private fun getCart(cartType: CartType): Cart? {
        return when (cartType) {
            CartType.DELIVERY -> deliveryCartResult.value
            CartType.PICKUP -> pickupCartResult.value
        }?.getOrNull()
    }

    private fun getBonusStateHolder(cartType: CartType): CartBonusAccountStateHolder {
        return when (cartType) {
            CartType.DELIVERY -> deliveryBonusAccountStateHolder
            CartType.PICKUP -> pickupBonusAccountStateHolder
        }
    }

    @AssistedFactory
    internal interface Factory {
        fun create(selectedCityResultFlow: Flow<CartSelectedCityResult?>): CartViewModel
    }

    private enum class Keys {
        SELECTED_CITY_RESULT;

        val key: String = name
    }

    companion object {
        private const val PRODUCT_COUNT_MAX_VALUE = 10
    }
}
