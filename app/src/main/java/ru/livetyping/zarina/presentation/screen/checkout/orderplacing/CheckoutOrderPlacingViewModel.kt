package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.CheckoutAddress
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.checkout.PickupPointDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.cart.model.CartRequest
import ru.livetyping.zarina.presentation.screen.cart.model.CartState
import ru.livetyping.zarina.presentation.screen.cart.model.CartStateBuilder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartBonusStateHolder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartMyCardStateHolder
import ru.livetyping.zarina.presentation.screen.cart.stateholder.CartPromoCodeStateHolder
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetCheckoutCartFlowUseCase
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

    private val params = savedStateHandle.toRoute<CheckoutGraph.OrderPlacing>(
        typeMap = CheckoutGraph.OrderPlacing.typeMap(),
    )
    private val checkoutParams = params.checkoutParams.toCheckoutParams()

    private val bonusStateHolder = CartBonusStateHolder(savedStateHandle)

    private val myCardStateHolder = CartMyCardStateHolder()

    private val promoCodeStateHolder = CartPromoCodeStateHolder(savedStateHandle)

    private val cartFlowRequester = FlowRequester(CartRequest.LOADING) {
        val params = GetCheckoutCartFlowUseCase.Params(checkoutParams)
        interactor.getCheckoutCartFlow(params)
    }

    private val cartResult: StateFlow<Result<Cart>?> = cartFlowRequester.flow
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

    val step: StateFlow<Int> = ImmutableStateFlow(params.step)

    val stepCount: StateFlow<Int> = ImmutableStateFlow(checkoutParams.cartType.checkoutStepCount)

    val customer: StateFlow<Customer> = ImmutableStateFlow(checkoutParams.customer)

    val deliveryInfo: StateFlow<DeliveryInfo> = ImmutableStateFlow(getDeliveryInfo(checkoutParams))

    val cartState: StateFlow<CartState> = combineMore(
        cartResult,
        cartFlowRequester.loadingState,
        bonusStateHolder.isBonusWriteOffApplied,
        myCardStateHolder.isMyCardApplied,
        promoCodeStateHolder.isPromoCodeInvalid,
        promoCodeStateHolder.promoCodeDescription,
    ) { result, loadingState, isBonusWriteOffApplied, isMyCardApplied, isPromoCodeInvalid, promoCodeDescription ->
        cartStateBuilder.build(
            cartResult = result,
            cartLoadingState = loadingState,
            cartType = checkoutParams.cartType,
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

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutOrderPlacingScreenAction) : SideEffect
    }

    @Immutable
    data class DeliveryInfo(
        val deliveryMethodType: DeliveryMethodType,
        val descriptions: List<String>,
    )

    companion object {
        private const val COMMA_SEPARATOR = ", "
    }
}
