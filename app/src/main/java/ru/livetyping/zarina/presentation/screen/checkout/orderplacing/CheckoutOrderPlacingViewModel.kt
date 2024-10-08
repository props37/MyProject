package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.CheckoutAddress
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.checkout.PickupPointDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.cart.model.CartRequest
import ru.livetyping.zarina.presentation.screen.cart.model.CartState
import ru.livetyping.zarina.presentation.screen.cart.model.CartStateBuilder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartBonusStateHolder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartMyCardStateHolder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartPromoCodeStateHolder
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.ApplyBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.ApplyMyCardToCartUseCase
import ru.livetyping.zarina.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.usecase.cart.RemoveBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.RemoveMyCardFromCartUseCase
import ru.livetyping.zarina.usecase.checkout.GetCheckoutCartFlowUseCase
import ru.livetyping.zarina.usecase.checkout.GetPaymentMethodsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.combineMore
import javax.inject.Inject

@HiltViewModel
class CheckoutOrderPlacingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutOrderPlacingInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartStateBuilder = CartStateBuilder()

    private var bonusJob: Job? = null
    private var myCardJob: Job? = null
    private var promoCodeJob: Job? = null

    private val params = savedStateHandle.toRoute<CheckoutGraph.OrderPlacing>(
        typeMap = CheckoutGraph.OrderPlacing.typeMap(),
    )
    private val checkoutParams = params.checkoutParams.toCheckoutParams()
    private val cartType = checkoutParams.cartType

    private val bonusStateHolder = CartBonusStateHolder(savedStateHandle)

    private val myCardStateHolder = CartMyCardStateHolder()

    private val promoCodeStateHolder = CartPromoCodeStateHolder(savedStateHandle)

    private val cartRequester = FlowRequester(CartRequest.LOADING) {
        val params = GetCheckoutCartFlowUseCase.Params(checkoutParams)
        interactor.getCheckoutCartFlow(params)
    }

    private val cartResult: StateFlow<Result<Cart>?> = cartRequester.flow
        .onEach { result ->
            val cart = result.getOrNull()
            if (cart != null) {
                bonusStateHolder.updateFromCart(cart)
                myCardStateHolder.updateFromCart(cart)
                promoCodeStateHolder.updateFromCart(cart)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val paymentMethodsFlowRequester = FlowRequester(PaymentMethodsRequest) {
        cartResult.flatMapLatest { result ->
            val cart = result?.getOrNull()
            if (cart != null) {
                val params = GetPaymentMethodsFlowUseCase.Params(checkoutParams, cart)
                interactor.getPaymentMethodsFlow(params)
            } else {
                flowOf(null)
            }
        }
    }

    private val paymentMethodsResult: StateFlow<Result<List<PaymentMethod>>?> =
        paymentMethodsFlowRequester.flow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val selectedPaymentMethodId = MutableStateFlow<PaymentMethod.Id?>(null)

    val paymentMethodsState: StateFlow<PaymentMethodsState> = combine(
        paymentMethodsResult,
        paymentMethodsFlowRequester.loadingState,
        selectedPaymentMethodId,
    ) { paymentMethodsResult, paymentMethodsLoadingState, selectedPaymentMethodId ->
        createPaymentMethodsState(
            paymentMethodsResult = paymentMethodsResult,
            paymentMethodsLoadingState = paymentMethodsLoadingState,
            selectedPaymentMethodId = selectedPaymentMethodId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = PaymentMethodsState.Loading,
    )

    val step: StateFlow<Int> = ImmutableStateFlow(params.step)

    val stepCount: StateFlow<Int> = ImmutableStateFlow(cartType.checkoutStepCount)

    val customer: StateFlow<Customer> = ImmutableStateFlow(checkoutParams.customer)

    val deliveryInfo: StateFlow<DeliveryInfo> = ImmutableStateFlow(getDeliveryInfo(checkoutParams))

    val cartState: StateFlow<CartState> = combineMore(
        cartResult,
        cartRequester.loadingState,
        bonusStateHolder.isBonusWriteOffApplied,
        myCardStateHolder.isMyCardApplied,
        promoCodeStateHolder.isPromoCodeInvalid,
        promoCodeStateHolder.promoCodeDescription,
    ) { result, loadingState, isBonusWriteOffApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        cartStateBuilder.build(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = cartType,
            isBonusWriteOffApplied = isBonusWriteOffApplied,
            bonusWriteOffTextFieldState = bonusStateHolder.bonusWriteOffTextFieldState,
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

    val isRefreshing: StateFlow<Boolean> = cartRequester.loadingState
        .map { loadingState ->
            loadingState.loadingRequest == CartRequest.REFRESHING
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    val isPullRefreshing: StateFlow<Boolean> = cartRequester.loadingState
        .map { loadingState ->
            loadingState.loadingRequest == CartRequest.PULL_REFRESHING
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    private val _isPaymentMethodSelectorBottomSheetVisible = MutableStateFlow(false)
    val isPaymentMethodSelectorBottomSheetVisible: StateFlow<Boolean> =
        _isPaymentMethodSelectorBottomSheetVisible.asStateFlow()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutOrderPlacingScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutOrderPlacingScreenAction.CheckoutClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onChangeCustomerClicked() {
        navigationThrottler.throttle {
            val action = CheckoutOrderPlacingScreenAction.ChangeCustomerClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onChangeDeliveryClicked() {
        navigationThrottler.throttle {
            val action = CheckoutOrderPlacingScreenAction.ChangeDeliveryClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onIsBonusWriteOffAppliedChanged(isApplied: Boolean) {
        if (bonusJob?.isActive == true) return

        bonusJob = viewModelScope.launch {
            if (isApplied) {
                val cart = cartResult.value?.getOrNull()
                val maxBonusCountToWriteOff = cart?.bonuses?.writeOff?.max ?: return@launch
                applyBonusWriteOff(maxBonusCountToWriteOff)
            } else {
                removeBonusWriteOff()
            }
        }
    }

    fun onBonusCountToWriteOffChanged(bonusCount: Int?) {
        if (bonusJob?.isActive == true) return

        val maxBonusCount = cartResult.value?.getOrNull()?.bonuses?.writeOff?.max ?: return
        bonusJob = viewModelScope.launch {
            if (bonusCount != null) {
                applyBonusWriteOff(bonusCount.coerceAtMost(maxBonusCount))
            } else {
                removeBonusWriteOff()
            }
        }
    }

    fun onIsMyCardAppliedChanged(isApplied: Boolean) {
        if (myCardJob?.isActive == true) return

        myCardStateHolder.setIsMyCardApplied(isApplied)
        myCardJob = viewModelScope.launch {
            val cart = cartResult.value?.getOrNull()
            if (isApplied) {
                val productsFirstPriceSum = cart?.myCard?.productsFirstPriceSum ?: return@launch
                applyMyCardToCart(productsFirstPriceSum)
            } else {
                removeMyCardFromCart()
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
                    cartRequester.request(CartRequest.REFRESHING)
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
            (cartState.value as? CartState.Cart)?.promoCodeState?.isApplied == true
        if (isPromoCodeApplied) {
            emitSideEffect(SideEffect.HideKeyboard)
            promoCodeJob = viewModelScope.launch {
                interactor.removePromoCode()
                    .onSuccess {
                        cartRequester.request(CartRequest.REFRESHING)
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

    fun onCartErrorRefreshClicked() {
        cartRequester.request(CartRequest.LOADING)
    }

    fun onPaymentMethodSelectorClicked() {
        _isPaymentMethodSelectorBottomSheetVisible.value = true
    }

    fun onPaymentMethodSelectorDismissRequested() {
        _isPaymentMethodSelectorBottomSheetVisible.value = false
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        selectedPaymentMethodId.value = paymentMethod.id
        cartRequester.request(CartRequest.REFRESHING)
    }

    fun onPaymentMethodsErrorRefreshClicked() {
        paymentMethodsFlowRequester.request(PaymentMethodsRequest)
    }

    fun onPayClicked() {
        if (selectedPaymentMethodId.value == null) {
            _isPaymentMethodSelectorBottomSheetVisible.value = true
            return
        }

        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onPullRefreshTriggered() {
        if (!cartRequester.loadingState.value.isLoading()) {
            cartRequester.request(CartRequest.PULL_REFRESHING)
        }
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            val action = SideEffect.OpenUrl(url)
            emitSideEffect(action)
        }
    }

    private suspend fun applyBonusWriteOff(bonusCount: Int) {
        val params = ApplyBonusWriteOffUseCase.Params(cartType, bonusCount)
        interactor.applyBonusWriteOff(params)
            .onSuccess {
                emitSideEffect(SideEffect.HideKeyboard)
                cartRequester.request(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.bonus_write_off_applying_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun removeBonusWriteOff() {
        val params = RemoveBonusWriteOffUseCase.Params(cartType)
        interactor.removeBonusWriteOff(params)
            .onSuccess {
                emitSideEffect(SideEffect.HideKeyboard)
                cartRequester.request(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.bonus_write_off_removing_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun applyMyCardToCart(productsFirstPriceSum: Int) {
        val cart = cartResult.value?.getOrNull()
        val isBonusWriteOffApplied = bonusStateHolder.isBonusWriteOffApplied.value
        val isPromoCodeApplied = cart?.promoCode?.isApplied == true

        val params = ApplyMyCardToCartUseCase.Params(cartType, productsFirstPriceSum)
        interactor.applyMyCardToCart(params)
            .onSuccess {
                cartRequester.request(CartRequest.REFRESHING)
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

    private suspend fun removeMyCardFromCart() {
        val params = RemoveMyCardFromCartUseCase.Params(cartType)
        interactor.removeMyCardFromCart(params)
            .onSuccess {
                cartRequester.request(CartRequest.REFRESHING)
            }
            .onFailure {
                myCardStateHolder.setIsMyCardApplied(false)
                val messageText = Text.Resource(R.string.my_card_canceling_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
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

    private fun getDeliveryInfo(checkoutParams: CheckoutParams): DeliveryInfo {
        val descriptions = when (checkoutParams) {
            is CourierDeliveryCheckoutParams -> {
                listOf(
                    getDeliveryAddressDescription(checkoutParams.address),
                    checkoutParams.dateTimePeriod.date,
                )
            }

            is PostDeliveryCheckoutParams -> {
                listOf(
                    getDeliveryAddressDescription(checkoutParams.address),
                    checkoutParams.dateTimePeriod.date,
                )
            }

            is PickupPointDeliveryCheckoutParams -> {
                listOf(
                    checkoutParams.pickupPoint.address,
                    checkoutParams.pickupPoint.expectedDeliveryDate,
                )
            }


            is StorePickupCheckoutParams -> {
                listOf(checkoutParams.store.name, checkoutParams.store.address)
            }
        }
        return DeliveryInfo(
            deliveryMethodType = checkoutParams.deliveryMethodType,
            descriptions = descriptions,
        )
    }

    private fun getDeliveryAddressDescription(address: CheckoutAddress): String {
        return buildString {
            append(address.city.name)
            append(COMMA_SEPARATOR)
            append(address.street.name)
            append(COMMA_SEPARATOR)
            append(address.building.name)
            if (address.apartment != null) {
                append(COMMA_SEPARATOR)
                append(address.apartment)
            }
        }
    }

    private fun createPaymentMethodsState(
        paymentMethodsResult: Result<List<PaymentMethod>>?,
        paymentMethodsLoadingState: FlowRequester.LoadingState,
        selectedPaymentMethodId: PaymentMethod.Id?,
    ): PaymentMethodsState {
        return if (paymentMethodsResult == null || paymentMethodsLoadingState.isLoading()) {
            PaymentMethodsState.Loading
        } else {
            paymentMethodsResult.fold(
                onSuccess = { paymentMethods ->
                    val selectedPaymentMethod = selectedPaymentMethodId?.let { selectedId ->
                        paymentMethods.find { it.id == selectedId }
                    }
                    PaymentMethodsState.Success(
                        paymentMethods = paymentMethods,
                        selectedPaymentMethod = selectedPaymentMethod,
                    )
                },
                onFailure = { t ->
                    val errorState = ErrorState.from(t)
                    PaymentMethodsState.Error(errorState)
                },
            )
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutOrderPlacingScreenAction) : SideEffect

        data object HideKeyboard : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect
    }

    @Immutable
    data class DeliveryInfo(
        val deliveryMethodType: DeliveryMethodType,
        val descriptions: List<String>,
    )

    @Stable
    sealed class PaymentMethodsState {
        @Immutable
        data class Success(
            val paymentMethods: List<PaymentMethod>,
            val selectedPaymentMethod: PaymentMethod?,
        ) : PaymentMethodsState()

        @Immutable
        data object Loading : PaymentMethodsState()

        @Immutable
        data class Error(val errorState: ErrorState) : PaymentMethodsState()

        fun findSelectedPaymentMethod(): PaymentMethod? {
            return (this as? Success)?.selectedPaymentMethod
        }
    }

    private data object PaymentMethodsRequest : FlowRequester.Request

    companion object {
        private const val COMMA_SEPARATOR = ", "
    }
}
