package ru.livetyping.zarina.presentation.screen.cart

import android.os.SystemClock
import androidx.compose.foundation.text.input.TextFieldState
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
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartSize
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.navigation.destination.graph.CartGraph
import ru.livetyping.zarina.presentation.screen.cart.CartViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.cart.model.CartRequest
import ru.livetyping.zarina.presentation.screen.cart.model.CartState
import ru.livetyping.zarina.presentation.screen.cart.model.CartStateBuilder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartBonusStateHolder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartMyCardStateHolder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartPromoCodeStateHolder
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
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.combineMore
import ru.livetyping.zarina.util.library.coroutines.mapState
import kotlin.time.Duration.Companion.minutes
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

    private val cartStateBuilder = CartStateBuilder()

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
        ImmutableStateFlow(CartType.entries.toImmutableList())

    private val _currentCartType = MutableStateFlow(CartType.DELIVERY)
    val currentCartType: StateFlow<CartType> = _currentCartType.asStateFlow()

    private val deliveryBonusStateHolder = CartBonusStateHolder(savedStateHandle)
    private val pickupBonusStateHolder = CartBonusStateHolder(savedStateHandle)

    private val myCardStateHolder = CartMyCardStateHolder()

    private val promoCodeStateHolder = CartPromoCodeStateHolder(savedStateHandle)

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
                deliveryBonusStateHolder.updateFromCart(cart)
                myCardStateHolder.updateFromCart(cart)
                promoCodeStateHolder.updateFromCart(cart)
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
                deliveryBonusStateHolder.updateFromCart(cart)
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
        deliveryCart?.size ?: pickupCart?.size ?: CartSize.EMPTY
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CartSize.EMPTY,
    )

    val deliveryCartState: StateFlow<CartState> = combineMore(
        deliveryCartResult,
        deliveryCartRequester.loadingState,
        deliveryBonusStateHolder.isBonusWriteOffApplied,
        myCardStateHolder.isMyCardApplied,
        promoCodeStateHolder.isPromoCodeInvalid,
        promoCodeStateHolder.promoCodeDescription,
    ) { result, loadingState, isBonusWriteOffApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        cartStateBuilder.build(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = CartType.DELIVERY,
            isBonusWriteOffApplied = isBonusWriteOffApplied,
            bonusWriteOffTextFieldState = deliveryBonusStateHolder.bonusWriteOffTextFieldState,
            isMyCardApplied = isMyCardApplied,
            promoCodeTextFieldState = promoCodeStateHolder.promoCodeTextFieldState,
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
        pickupBonusStateHolder.isBonusWriteOffApplied,
        myCardStateHolder.isMyCardApplied,
        promoCodeStateHolder.isPromoCodeInvalid,
        promoCodeStateHolder.promoCodeDescription,
    ) { result, loadingState, isBonusWriteOffApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        cartStateBuilder.build(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = CartType.PICKUP,
            isBonusWriteOffApplied = isBonusWriteOffApplied,
            bonusWriteOffTextFieldState = pickupBonusWriteOffTextFieldState,
            isMyCardApplied = isMyCardApplied,
            promoCodeTextFieldState = promoCodeStateHolder.promoCodeTextFieldState,
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
        started = SharingStarted.WhileUiSubscribed,
        initialValue = false,
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val pickupBonusWriteOffTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private var deliveryCartLastUpdateTimestampMillis = 0L
    private var pickupCartLastUpdateTimestampMillis = 0L

    init {
        resetPromoCodeErrorsOnChange()
        handleCitySelectorResult()
        handleProductCountSelectorResult()
    }

    fun onScreenOpened() {
        updateCarts()
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
        if (deliveryCartResult.value?.isFailure == true) {
            requestDeliveryCart(CartRequest.LOADING)
        }
        if (pickupCartResult.value?.isFailure == true) {
            requestPickupCart(CartRequest.LOADING)
        }
    }

    fun onIsBonusWriteOffAppliedChanged(isApplied: Boolean) {
        if (bonusJob?.isActive == true) return

        val cartType = currentCartType.value
        val bonusStateHolder = getBonusStateHolder(cartType)
        bonusStateHolder.setIsBonusWriteOffApplied(isApplied)
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

        myCardStateHolder.setIsMyCardApplied(isApplied)
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
            val promoCode = promoCodeStateHolder.promoCode
            val params = ApplyPromoCodeUseCase.Params(promoCode)
            interactor.applyPromoCode(params)
                .onSuccess {
                    emitSideEffect(SideEffect.HideKeyboard)
                    requestCarts(CartRequest.REFRESHING)
                }
                .onFailure {
                    val messageText = Text.Resource(R.string.promo_code_applying_error)
                    val message = ZarinaToastMessage.error(messageText)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))

                    // TODO: [High] Do only if PromoCodeNotFoundException is caught
                    promoCodeStateHolder.setIsPromoCodeInvalid(true)
                    val promoCodeDescription = Text.Resource(R.string.promo_code_applying_error_description)
                    promoCodeStateHolder.setPromoCodeDescription(promoCodeDescription)
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
                        requestCarts(CartRequest.REFRESHING)
                    }
                    .onFailure {
                        val messageText = Text.Resource(R.string.promo_code_removing_error)
                        val message = ZarinaToastMessage.error(messageText)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
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

    fun onPullRefreshTriggered() {
        if (!deliveryCartRequester.loadingState.value.isLoading()) {
            requestDeliveryCart(CartRequest.PULL_REFRESHING)
        }
        if (!pickupCartRequester.loadingState.value.isLoading()) {
            requestPickupCart(CartRequest.PULL_REFRESHING)
        }
    }

    private suspend fun applyBonusWriteOff(cartType: CartType, bonusCount: Int) {
        val params = ApplyBonusWriteOffUseCase.Params(cartType, bonusCount)
        interactor.applyBonusWriteOff(params)
            .onSuccess {
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
        val bonusStateHolder = getBonusStateHolder(cartType)
        val isBonusWriteOffApplied = bonusStateHolder.isBonusWriteOffApplied.value
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
                myCardStateHolder.setIsMyCardApplied(false)
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
                myCardStateHolder.setIsMyCardApplied(false)
                val messageText = Text.Resource(R.string.my_card_canceling_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private fun resetPromoCodeErrorsOnChange() {
        promoCodeStateHolder.promoCodeAsFlow
            .onEach {
                promoCodeStateHolder.setIsPromoCodeInvalid(false)
                promoCodeStateHolder.setPromoCodeDescription(null)
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

    private fun updateCarts() {
        val updateDeliveryCart = isCartUpdateNeeded(deliveryCartLastUpdateTimestampMillis)
                || deliveryCartResult.value?.isFailure == true
        val updatePickupCart = isCartUpdateNeeded(pickupCartLastUpdateTimestampMillis)
                || pickupCartResult.value?.isFailure == true
        if (updateDeliveryCart) {
            requestDeliveryCart(CartRequest.LOADING)
        }
        if (updatePickupCart) {
            requestPickupCart(CartRequest.LOADING)
        }
    }

    private fun requestCarts(request: CartRequest) {
        requestDeliveryCart(request)
        requestPickupCart(request)
    }

    private fun requestDeliveryCart(request: CartRequest) {
        deliveryCartRequester.request(request)
        deliveryCartLastUpdateTimestampMillis = getCurrentTimestampMillis()
    }

    private fun requestPickupCart(request: CartRequest) {
        pickupCartRequester.request(request)
        pickupCartLastUpdateTimestampMillis = getCurrentTimestampMillis()
    }

    private fun getCart(cartType: CartType): Cart? {
        return when (cartType) {
            CartType.DELIVERY -> deliveryCartResult.value
            CartType.PICKUP -> pickupCartResult.value
        }?.getOrNull()
    }

    private fun getBonusStateHolder(cartType: CartType): CartBonusStateHolder {
        return when (cartType) {
            CartType.DELIVERY -> deliveryBonusStateHolder
            CartType.PICKUP -> pickupBonusStateHolder
        }
    }

    private fun isCartUpdateNeeded(lastUpdateTimestampMillis: Long): Boolean {
        val currentTimestampMillis = getCurrentTimestampMillis()
        return currentTimestampMillis - lastUpdateTimestampMillis > CART_UPDATE_THRESHOLD_MILLIS
    }

    private fun getCurrentTimestampMillis(): Long {
        return SystemClock.elapsedRealtime()
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CartScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect

        data object HideKeyboard : SideEffect
    }

    @AssistedFactory
    interface Factory {
        fun create(
            citySelectorResultFlow: StateFlow<UnscopedDestinations.CitySelector.Result?>,
            productCountSelectorResultFlow: StateFlow<CartGraph.ProductCountSelector.Result?>,
        ): CartViewModel
    }

    companion object {
        private const val KEY_RESULT_CITY_SELECTOR_RESULT = "result_city_selector"
        private const val KEY_RESULT_PRODUCT_COUNT_SELECTOR = "result_product_count_selector"

        private val CART_UPDATE_THRESHOLD_MILLIS = 5.minutes.inWholeMilliseconds
    }
}
