package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
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
import ru.livetyping.zarina.presentation.screen.checkout.common.checkoutStepCount
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CheckoutOrderPlacingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutOrderPlacingInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val params = savedStateHandle.toRoute<CheckoutGraph.OrderPlacing>(
        typeMap = CheckoutGraph.OrderPlacing.typeMap(),
    )
    private val checkoutParams = params.checkoutParams.toCheckoutParams()

    val step: StateFlow<Int> = ImmutableStateFlow(params.step)

    val stepCount: StateFlow<Int> = ImmutableStateFlow(checkoutParams.cartType.checkoutStepCount)

    val customer: StateFlow<Customer> = ImmutableStateFlow(checkoutParams.customer)

    val deliveryInfo: StateFlow<DeliveryInfo> = ImmutableStateFlow(getDeliveryInfo(checkoutParams))

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
