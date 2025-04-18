package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
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
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaPaymentMethodType
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutStep
import ru.livetyping.zarina.core.domain.model.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PayturePaymentData
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromPickupPointCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.checkout.SberPaymentData
import ru.livetyping.zarina.core.domain.model.checkout.UrlPaymentData
import ru.livetyping.zarina.core.domain.model.checkout.exception.CartChangedException
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.geo.Address
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderStatus
import ru.livetyping.zarina.core.domain.usecase.cart.ApplyMyCardUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.CancelBonusRedemptionUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.RedeemBonusesUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.WithdrawMyCardUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.CheckoutUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.GetCheckoutCartFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPaymentMethodsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.UpdateOrderPaymentStatusUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.WithdrawGiftCertificateUseCase
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderStatusUseCase
import ru.livetyping.zarina.core.navigationutil.ScreenResultHandler
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.formatPrice
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.cart.ui.api.PaymentResult
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartRequest
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartStateBuilder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel.CartBonusAccountStateHolder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel.CartMyCardStateHolder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.viewmodel.CartPromoCodeStateHolder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.GiftCertificateScreenResult
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import java.math.BigDecimal
import kotlin.time.Duration.Companion.seconds
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel(assistedFactory = OrderPlacingViewModel.Factory::class)
internal class OrderPlacingViewModel @AssistedInject constructor(
    @Assisted
    private val giftCertificateResultFlow: Flow<GiftCertificateScreenResult?>,
    @Assisted
    private val paymentResultFlow: Flow<PaymentResult?>,
    savedStateHandle: SavedStateHandle,
    private val deps: OrderPlacingDependencies,
) : ViewModel(), SideEffectSource<OrderPlacingSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val cartStateBuilder = CartStateBuilder()

    private var bonusJob: Job? = null
    private var myCardJob: Job? = null
    private var promoCodeJob: Job? = null
    private var checkoutJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<OrderPlacingNavEntry>(
        typeMap = OrderPlacingNavEntry.typeMap(),
    )
    private val checkoutParams = navEntry.checkoutParams.toCheckoutParams()
    private val cartType = checkoutParams.cartType

    private val bonusStateHolder = CartBonusAccountStateHolder(savedStateHandle)

    private val myCardStateHolder = CartMyCardStateHolder()

    private val promoCodeStateHolder = CartPromoCodeStateHolder(savedStateHandle)

    private val selectedPaymentMethod = MutableStateFlow<PaymentMethod?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val cartRequester = FlowRequester(CartRequest.LOADING) { request ->
        selectedPaymentMethod.flatMapLatest { paymentMethod ->
            markAsLoading(request)
            val params = GetCheckoutCartFlowUseCase.Params(checkoutParams, paymentMethod)
            deps.getCheckoutCartFlow(params)
        }
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

    private val cart: Cart?
        get() = cartResult.value?.getOrNull()

    private val availablePaymentMethods: List<PaymentMethod>
        get() = paymentMethodsResult.value?.getOrNull().orEmpty()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val paymentMethodsFlowRequester = FlowRequester(PaymentMethodsRequest) {
        cartResult.flatMapLatest { result ->
            val cart = result?.getOrNull()
            if (cart != null) {
                val params = GetPaymentMethodsFlowUseCase.Params(checkoutParams, cart)
                deps.getPaymentMethodsFlow(params)
            } else {
                flowOf(null)
            }
        }
    }

    private val paymentMethodsResult: StateFlow<Result<List<PaymentMethod>>?> =
        paymentMethodsFlowRequester.flow
            .onEach { result ->
                // Check if the cart contains applied gift certificate
                val cart = cart
                if (selectedPaymentMethod.value == null && cart?.giftCertificate != null) {
                    val paymentMethods = result?.getOrNull()
                    selectedPaymentMethod.value = paymentMethods?.find {
                        it.type == PaymentMethodType.GIFT_CERTIFICATE
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val paymentMethodsState: StateFlow<PaymentMethodsState> = combine(
        paymentMethodsResult,
        paymentMethodsFlowRequester.loadingState,
        selectedPaymentMethod,
    ) { paymentMethodsResult, paymentMethodsLoadingState, selectedPaymentMethod ->
        createPaymentMethodsState(
            paymentMethodsResult = paymentMethodsResult,
            paymentMethodsLoadingState = paymentMethodsLoadingState,
            selectedPaymentMethod = selectedPaymentMethod,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PaymentMethodsState.Loading,
    )

    val step: StateFlow<Int> = ReadOnlyStateFlow(navEntry.checkoutStep)

    val stepCount: StateFlow<Int> = ReadOnlyStateFlow(cartType.checkoutStepCount)

    val recipient: StateFlow<Recipient> = ReadOnlyStateFlow(checkoutParams.recipient)

    val deliveryInfo: StateFlow<DeliveryInfo> = ReadOnlyStateFlow(getDeliveryInfo(checkoutParams))

    val cartState: StateFlow<CartState> = combineMore(
        cartResult,
        cartRequester.loadingState,
        bonusStateHolder.isBonusRedemptionApplied,
        myCardStateHolder.isMyCardApplied,
        promoCodeStateHolder.isPromoCodeInvalid,
        promoCodeStateHolder.promoCodeDescription,
    ) { result, loadingState, isBonusRedemptionApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        cartStateBuilder.build(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = cartType,
            isBonusRedemptionApplied = isBonusRedemptionApplied,
            bonusRedemptionTextFieldState = bonusStateHolder.bonusRedemptionTextFieldState,
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
        cartRequester.loadingState,
        operationTracker.isOperationOngoing(Operation.CHECKOUT),
    ) { cartLoadingState, isCheckoutOngoing ->
        val isCartLoading = cartLoadingState.loadingRequest == CartRequest.REFRESHING
        isCartLoading || isCheckoutOngoing
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = false,
    )

    val isPullRefreshing: StateFlow<Boolean> = cartRequester.loadingState
        .map { loadingState ->
            loadingState.loadingRequest == CartRequest.PULL_REFRESHING
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = false,
        )

    private val _isPaymentMethodSelectorBottomSheetVisible = MutableStateFlow(false)
    val isPaymentMethodSelectorBottomSheetVisible: StateFlow<Boolean> =
        _isPaymentMethodSelectorBottomSheetVisible.asStateFlow()

    val isPayButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.CHECKOUT)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = false,
        )

    private val _infoModalBottomSheetState = MutableStateFlow<InfoModalBottomSheetState?>(null)
    val infoModalBottomSheetState: StateFlow<InfoModalBottomSheetState?> = _infoModalBottomSheetState.asStateFlow()

    private var currentCheckoutStep: CheckoutStep? = null

    private var wasPaymentClosed = false
    private var paymentStatusCheckCountAfterPaymentClosed = 0

    init {
        handleGiftCertificateResult()
        handlePaymentResult()
    }

    fun onScreenOpened() {
        val currentCheckoutStep = currentCheckoutStep
        if (currentCheckoutStep is CheckoutStep.CheckoutCompleted) {
            viewModelScope.launch {
                completeCheckout(currentCheckoutStep)
            }
        }
    }

    fun onBackClicked() {
        if (checkoutJob?.isActive == true) return

        navigationThrottler.throttle {
            val action = OrderPlacingScreenAction.BackClicked
            emitSideEffect(OrderPlacingSideEffect.Navigate(action))
        }
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = OrderPlacingScreenAction.CloseClicked
            emitSideEffect(OrderPlacingSideEffect.Navigate(action))
        }
    }

    fun onChangeCustomerClicked() {
        navigationThrottler.throttle {
            val action = OrderPlacingScreenAction.ChangeRecipientClicked
            emitSideEffect(OrderPlacingSideEffect.Navigate(action))
        }
    }

    fun onChangeDeliveryClicked() {
        navigationThrottler.throttle {
            val action = OrderPlacingScreenAction.ChangeDeliveryClicked
            emitSideEffect(OrderPlacingSideEffect.Navigate(action))
        }
    }

    fun onIsBonusWriteOffAppliedChanged(isApplied: Boolean) {
        if (bonusJob?.isActive == true) return

        bonusJob = viewModelScope.launch {
            if (isApplied) {
                val cart = cartResult.value?.getOrNull()
                val maxBonusCountToWriteOff = cart?.bonusAccount?.redemption?.max ?: return@launch
                applyBonusWriteOff(maxBonusCountToWriteOff)
            } else {
                cancelBonusRedemption()
            }
        }
    }

    fun onBonusCountToWriteOffChanged(bonusCount: Int?) {
        if (bonusJob?.isActive == true) return

        val maxBonusCount = cartResult.value?.getOrNull()?.bonusAccount?.redemption?.max ?: return
        bonusJob = viewModelScope.launch {
            if (bonusCount != null) {
                applyBonusWriteOff(bonusCount.coerceAtMost(maxBonusCount))
            } else {
                cancelBonusRedemption()
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
                withdrawMyCard()
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
                    emitSideEffect(OrderPlacingSideEffect.HideKeyboard)
                    cartRequester.request(CartRequest.REFRESHING)
                }
                .onFailure {
                    val messageText = Text.Resource(R.string.cart_promo_code_applying_error)
                    val message = ZarinaToastMessage.error(messageText)
                    emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))

                    // TODO: [High] Do only if PromoCodeNotFoundException is caught
                    promoCodeStateHolder.setIsPromoCodeInvalid(true)
                    val promoCodeDescription = Text.Resource(R.string.cart_promo_code_applying_error_description)
                    promoCodeStateHolder.setPromoCodeDescription(promoCodeDescription)
                }
        }
    }

    fun onRemovePromoCodeClicked() {
        if (promoCodeJob?.isActive == true) return

        val isPromoCodeApplied =
            (cartState.value as? CartState.Cart)?.promoCodeState?.isApplied == true
        if (isPromoCodeApplied) {
            emitSideEffect(OrderPlacingSideEffect.HideKeyboard)
            promoCodeJob = viewModelScope.launch {
                deps.removePromoCode()
                    .onSuccess {
                        cartRequester.request(CartRequest.REFRESHING)
                    }
                    .onFailure {
                        val messageText = Text.Resource(R.string.cart_promo_code_withdraw_error)
                        val message = ZarinaToastMessage.error(messageText)
                        emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
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
        val currentPaymentMethod = selectedPaymentMethod.value

        if (paymentMethod.type != currentPaymentMethod?.type) {
            val appMetricaType = paymentMethod.type.toAppMetricaPaymentMethodType()
            deps.appMetrica.reportPaymentMethodSelected(appMetricaType)
        }

        when {
            paymentMethod.type == PaymentMethodType.GIFT_CERTIFICATE
                    && currentPaymentMethod?.type != PaymentMethodType.GIFT_CERTIFICATE -> {
                val cart = cart ?: return
                navigationThrottler.throttle {
                    val action = OrderPlacingScreenAction.GiftCertificateSelected(
                        cartType = checkoutParams.cartType,
                        cart = cart,
                    )
                    emitSideEffect(OrderPlacingSideEffect.Navigate(action))
                }
            }

            paymentMethod.type != PaymentMethodType.GIFT_CERTIFICATE
                    && currentPaymentMethod?.type == PaymentMethodType.GIFT_CERTIFICATE -> {
                viewModelScope.launch {
                    val params = WithdrawGiftCertificateUseCase.Params(
                        paymentMethodType = paymentMethod.type,
                    )
                    deps.withdrawGiftCertificate(params)
                        .onSuccess {
                            selectedPaymentMethod.value = paymentMethod
                        }
                        .onFailure {
                            val messageText = Text.Resource(R.string.cart_gift_certificate_withdraw_error)
                            val message = ZarinaToastMessage.error(messageText)
                            emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
                        }
                }
            }

            else -> selectedPaymentMethod.value = paymentMethod
        }
    }

    fun onPaymentMethodsErrorRefreshClicked() {
        paymentMethodsFlowRequester.request(PaymentMethodsRequest)
    }

    fun onPayClicked() {
        val paymentMethod = selectedPaymentMethod.value
        if (paymentMethod == null) {
            _isPaymentMethodSelectorBottomSheetVisible.value = true
            return
        }

        val cart = cart
        if (cart == null) {
            val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
            val message = ZarinaToastMessage.error(messageText)
            emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
            return
        }

        checkout(cart, paymentMethod, availablePaymentMethods)
    }

    fun onPullRefreshTriggered() {
        if (!cartRequester.loadingState.value.isLoading()) {
            cartRequester.request(CartRequest.PULL_REFRESHING)
        }
    }

    fun onInfoButtonClicked(button: InfoButton) {
        val cart = cart ?: return
        val text = when (button) {
            InfoButton.GIFT_CERTIFICATE_WRITE_OFF_SIZE -> {
                val giftCertificateWriteOffSize = cart.price.giftCertificateRedemptionValue ?: return
                val formattedGiftCertificateWriteOff = formatPrice(giftCertificateWriteOffSize)
                Text.Resource(
                    resId = R.string.cart_gift_certificate_write_off_info,
                    formattedGiftCertificateWriteOff
                )
            }

            InfoButton.FINAL_PRICE -> {
                val resId = if (cart.price.finalPrice > BigDecimal.ZERO) {
                    R.string.cart_gift_certificate_user_need_to_pay_difference
                } else {
                    R.string.cart_gift_certificate_covers_whole_price
                }
                Text.Resource(resId)
            }
        }

        _infoModalBottomSheetState.value = InfoModalBottomSheetState(text)
    }

    fun onInfoModalBottomSheetClosed() {
        _infoModalBottomSheetState.value = null
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            val action = OrderPlacingSideEffect.OpenUrl(url)
            emitSideEffect(action)
        }
    }

    private suspend fun applyBonusWriteOff(bonusCount: Int) {
        val params = RedeemBonusesUseCase.Params(cartType, bonusCount)
        deps.redeemBonuses(params)
            .onSuccess {
                emitSideEffect(OrderPlacingSideEffect.HideKeyboard)
                cartRequester.request(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.cart_bonus_redemption_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun cancelBonusRedemption() {
        val params = CancelBonusRedemptionUseCase.Params(cartType)
        deps.cancelBonusRedemption(params)
            .onSuccess {
                emitSideEffect(OrderPlacingSideEffect.HideKeyboard)
                cartRequester.request(CartRequest.REFRESHING)
            }
            .onFailure {
                val messageText = Text.Resource(R.string.cart_bonus_redemption_canceling_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun applyMyCardToCart(productsFirstPriceSum: Int) {
        val cart = cartResult.value?.getOrNull()
        val isBonusWriteOffApplied = bonusStateHolder.isBonusRedemptionApplied.value
        val isPromoCodeApplied = cart?.promoCode?.isApplied == true

        val params = ApplyMyCardUseCase.Params(cartType, productsFirstPriceSum)
        deps.applyMyCard(params)
            .onSuccess {
                cartRequester.request(CartRequest.REFRESHING)
                showMyCardReplacedOtherBonusToast(
                    isBonusWriteOffApplied = isBonusWriteOffApplied,
                    isPromoCodeApplied = isPromoCodeApplied,
                )
            }
            .onFailure {
                myCardStateHolder.setIsMyCardApplied(false)
                val messageText = Text.Resource(R.string.cart_my_card_applying_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
            }
    }

    private suspend fun withdrawMyCard() {
        val params = WithdrawMyCardUseCase.Params(cartType)
        deps.withdrawMyCard(params)
            .onSuccess {
                cartRequester.request(CartRequest.REFRESHING)
            }
            .onFailure {
                myCardStateHolder.setIsMyCardApplied(false)
                val messageText = Text.Resource(R.string.cart_my_card_withdraw_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
            }
    }

    private fun showMyCardReplacedOtherBonusToast(
        isBonusWriteOffApplied: Boolean,
        isPromoCodeApplied: Boolean,
    ) {
        val messageText = when {
            isBonusWriteOffApplied -> {
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
        emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
    }

    private fun checkout(
        cart: Cart,
        paymentMethod: PaymentMethod,
        availablePaymentMethods: List<PaymentMethod>,
    ) {
        if (checkoutJob?.isActive == true) return

        wasPaymentClosed = false
        paymentStatusCheckCountAfterPaymentClosed = 0

        checkoutJob = viewModelScope.launch {
            operationTracker.track(Operation.CHECKOUT) {
                val params = CheckoutUseCase.Params(
                    cart = cart,
                    paymentMethod = paymentMethod,
                    availablePaymentMethods = availablePaymentMethods,
                    checkoutParams = checkoutParams,
                )
                deps.checkout(params).collect { checkoutStepResult ->
                    checkoutStepResult
                        .onSuccess { onCheckoutStep(it) }
                        .onFailure(::onCheckoutFailure)
                }
            }
        }
    }

    private suspend fun onCheckoutStep(stage: CheckoutStep) {
        currentCheckoutStep = stage
        when (stage) {
            is CheckoutStep.PaymentStarted -> {
                val paymentUrl = when (val data = stage.paymentData) {
                    is PayturePaymentData -> data.paymentUrl
                    is SberPaymentData -> data.paymentUrl
                    is UrlPaymentData -> data.paymentUrl
                }
                val action = OrderPlacingScreenAction.PaymentStarted(paymentUrl)
                emitSideEffect(OrderPlacingSideEffect.Navigate(action))
            }

            CheckoutStep.PaymentStatusChecked -> {
                if (wasPaymentClosed) paymentStatusCheckCountAfterPaymentClosed++
                if (paymentStatusCheckCountAfterPaymentClosed > PAYMENT_STATUS_CHECK_COUNT_LIMIT_AFTER_PAYMENT_CLOSED) {
                    checkoutJob?.cancel()
                    val text = Text.Resource(R.string.cart_payment_status_check_error)
                    val message = ZarinaToastMessage.error(
                        text = text,
                        duration = 5.seconds,
                    )
                    emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
                }
            }

            CheckoutStep.PaymentCompleted -> Unit
            is CheckoutStep.CheckoutCompleted -> {
                if (!stage.shouldAwaitPaymentCompleted) {
                    completeCheckout(stage)
                }
            }
        }
    }

    private fun onCheckoutFailure(t: Throwable) {
        if (t is CartChangedException) {
            cartRequester.request(CartRequest.REFRESHING)
        }

        val messageText = when (t) {
            is CartChangedException -> Text.Resource(R.string.cart_has_changed_error)
            else -> Text.Resource(RCommon.string.res_something_went_wrong)
        }
        val message = ZarinaToastMessage.error(messageText, ZarinaToastMessage.DURATION_LONG)
        emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
    }

    private suspend fun completeCheckout(completedCheckoutStep: CheckoutStep.CheckoutCompleted) {
        var order = completedCheckoutStep.order
        operationTracker.track(Operation.CHECKOUT) {
            if (completedCheckoutStep.shouldUpdateOrderStatus) {
                updateOrderPaymentStatus(order.id, order.paymentMethodType)
                val updatedOrderStatus = getOrderStatus(order.id)
                if (updatedOrderStatus != null) {
                    order = order.copy(status = updatedOrderStatus)
                }
            }
            val action = OrderPlacingScreenAction.OrderConfirmed(order)
            emitSideEffect(OrderPlacingSideEffect.Navigate(action))
        }
    }

    private suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    ) {
        val params = UpdateOrderPaymentStatusUseCase.Params(orderId, paymentMethodType)
        deps.updateOrderPaymentStatus(params)
    }

    private suspend fun getOrderStatus(orderId: Order.Id): OrderStatus? {
        val params = GetOrderStatusUseCase.Params(orderId)
        return deps.getOrderStatus(params).getOrNull()
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

            is PickupFromPickupPointCheckoutParams -> {
                listOf(
                    checkoutParams.pickupPoint.address,
                    checkoutParams.pickupPoint.expectedDeliveryDate,
                )
            }


            is PickupFromStoreCheckoutParams -> {
                listOf(checkoutParams.store.name, checkoutParams.store.address)
            }
        }
        return DeliveryInfo(
            deliveryMethodType = checkoutParams.deliveryMethod.type,
            descriptions = descriptions,
        )
    }

    private fun getDeliveryAddressDescription(address: Address): String {
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
        selectedPaymentMethod: PaymentMethod?,
    ): PaymentMethodsState {
        return if (paymentMethodsResult == null || paymentMethodsLoadingState.isLoading()) {
            PaymentMethodsState.Loading
        } else {
            paymentMethodsResult.fold(
                onSuccess = { paymentMethods ->
                    @Suppress("NAME_SHADOWING")
                    val selectedPaymentMethod = selectedPaymentMethod?.let { paymentMethod ->
                        paymentMethods.find { it.type == paymentMethod.type }
                    }
                    PaymentMethodsState.Success(
                        paymentMethods = paymentMethods,
                        selectedPaymentMethod = selectedPaymentMethod,
                    )
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    PaymentMethodsState.Error(errorState)
                },
            )
        }
    }

    private fun handleGiftCertificateResult() {
        viewModelScope.launch {
            screenResultHandler.handle<GiftCertificateScreenResult>(
                resultFlow = giftCertificateResultFlow,
                key = KEY_RESULT_GIFT_CERTIFICATE_RESULT,
            ) { result ->
                if (result.isGiftCertificateApplied) {
                    val paymentMethods = paymentMethodsResult.value?.getOrNull()
                    val giftCertificatePaymentMethod = paymentMethods?.find {
                        it.type == PaymentMethodType.GIFT_CERTIFICATE
                    }
                    if (giftCertificatePaymentMethod != null) {
                        selectedPaymentMethod.value = giftCertificatePaymentMethod
                        cartRequester.request(CartRequest.REFRESHING)
                    } else {
                        val messageText = Text.Resource(R.string.cart_gift_certificate_applying_error)
                        val message = ZarinaToastMessage.error(messageText)
                        emitSideEffect(OrderPlacingSideEffect.ShowZarinaToast(message))
                        paymentMethodsFlowRequester.request(PaymentMethodsRequest)
                    }
                }
            }
        }
    }

    private fun handlePaymentResult() {
        viewModelScope.launch {
            screenResultHandler.handle<PaymentResult>(
                resultFlow = paymentResultFlow,
                key = KEY_RESULT_PAYMENT,
            ) { _ ->
                wasPaymentClosed = true
            }
        }
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
        data class Error(val errorState: ZarinaErrorScreenState) : PaymentMethodsState()

        fun findSelectedPaymentMethod(): PaymentMethod? {
            return (this as? Success)?.selectedPaymentMethod
        }
    }

    @Immutable
    data class InfoModalBottomSheetState(val text: Text)

    enum class InfoButton {
        GIFT_CERTIFICATE_WRITE_OFF_SIZE,
        FINAL_PRICE,
    }

    private data object PaymentMethodsRequest : FlowRequest

    private enum class Operation : OperationKey { CHECKOUT }

    @AssistedFactory
    interface Factory {
        fun create(
            giftCertificateResultFlow: Flow<GiftCertificateScreenResult?>,
            paymentResultFlow: Flow<PaymentResult?>,
        ): OrderPlacingViewModel
    }

    companion object {
        private const val COMMA_SEPARATOR = ", "
        private const val PAYMENT_STATUS_CHECK_COUNT_LIMIT_AFTER_PAYMENT_CLOSED = 1

        private const val KEY_RESULT_GIFT_CERTIFICATE_RESULT = "result_gift_certificate"
        private const val KEY_RESULT_PAYMENT = "result_payment"
    }
}
