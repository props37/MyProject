package ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CheckoutOrderConfirmedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val orderConfirmed = savedStateHandle.toRoute<CheckoutGraph.OrderConfirmed>(
        typeMap = CheckoutGraph.OrderConfirmed.typeMap(),
    )

    val order: StateFlow<OrderDetails> = ImmutableStateFlow(orderConfirmed.order.toOrderDetails())

    fun onReturnToHomeScreenClicked() {
        navigationThrottler.throttle {
            val action = CheckoutOrderConfirmedScreenAction.ReturnToHomeScreen
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onPayForOrderClicked() {
        val paymentUrl = order.value.paymentUrl
        if (paymentUrl != null) {
            navigationThrottler.throttle {
                val action = CheckoutOrderConfirmedScreenAction.PaymentStarted(paymentUrl)
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            Timber.e("Failed to start payment because paymentUrl is null")
            val messageText = Text.Resource(R.string.something_went_wrong)
            val message = ZarinaToastMessage.error(messageText)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
        }
    }

    fun onPhoneNumberClicked(phone: PhoneNumber) {
        navigationThrottler.throttle {
            val sideEffect = SideEffect.DialPhoneNumber(phone)
            emitSideEffect(sideEffect)
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutOrderConfirmedScreenAction) : SideEffect

        data class DialPhoneNumber(val phoneNumber: PhoneNumber) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }
}
