package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model.ButtonType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model.DescriptionType
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model.OrderConfirmedState
import javax.inject.Inject

@HiltViewModel
internal class OrderConfirmedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
) : ViewModel(), SideEffectSource<OrderConfirmedSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<OrderConfirmedNavEntry>(
        typeMap = OrderConfirmedNavEntry.typeMap(),
    )

    private val order = navEntry.order.toOrderDetailed()

    val orderConfirmedState = ReadOnlyStateFlow(
        OrderConfirmedState(
            order = order,
            descriptionType = getDescriptionType(order),
            buttonType = getButtonType(order),
        )
    )

    init {
        refreshCart()
    }

    fun onReturnToHomeClicked() {
        navigationThrottler.throttle {
            val action = OrderConfirmedScreenAction.ReturnToHomeClicked
            emitSideEffect(OrderConfirmedSideEffect.Navigate(action))
        }
    }

    fun onPayClicked() {
        val paymentUrl = order.paymentUrl
        if (paymentUrl != null) {
            navigationThrottler.throttle {
                val action = OrderConfirmedScreenAction.PayClicked(paymentUrl)
                emitSideEffect(OrderConfirmedSideEffect.Navigate(action))
            }
        } else {
            val text = Text.Resource(R.string.cart_payment_starting_error)
            val message = ZarinaToastMessage.error(text)
            emitSideEffect(OrderConfirmedSideEffect.ShowZarinaToast(message))
        }
    }

    private fun refreshCart() {
        viewModelScope.launch {
            val params = GetCartProductIdsFlowUseCase.Params(CachePolicy.Remote())
            getCartProductIdsFlow(params).firstOrNull()
        }
    }

    private fun getDescriptionType(order: OrderDetailed): DescriptionType {
        return when {
            order.isPaid -> DescriptionType.ORDER_PAID
            order.paymentMethodType == PaymentMethodType.POSTPAID -> {
                DescriptionType.ORDER_SHOULD_BE_PAID_UPON_RECEIPT
            }

            order.paymentMethodType == PaymentMethodType.FREE -> DescriptionType.ORDER_PAID
            else -> DescriptionType.ORDER_SHOULD_BE_PAID
        }
    }

    private fun getButtonType(order: OrderDetailed): ButtonType {
        return when {
            order.isPaid -> ButtonType.RETURN_TO_HOME
            order.paymentMethodType == PaymentMethodType.POSTPAID -> ButtonType.RETURN_TO_HOME
            order.paymentMethodType == PaymentMethodType.FREE -> ButtonType.RETURN_TO_HOME
            else -> ButtonType.PAY
        }
    }
}
