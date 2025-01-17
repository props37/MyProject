package ru.livetyping.zarina.presentation.screen.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.checkout.CardPaymentData
import ru.livetyping.zarina.domain.checkout.UrlPaymentData
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.payment.PaymentViewModel.SideEffect
import ru.livetyping.zarina.usecase.checkout.GetCompletedPaymentsFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCompletedPaymentsFlow: GetCompletedPaymentsFlowUseCase,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val payment = savedStateHandle.toRoute<UnscopedDestinations.Payment>()

    val paymentUrl: StateFlow<Url> = ImmutableStateFlow(Url(payment.paymentUrl))

    init {
        closeOnPaymentCompleted()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            closeScreen()
        }
    }

    private fun closeOnPaymentCompleted() {
        getCompletedPaymentsFlow()
            .onEach { result ->
                val paymentData = result.getOrNull() ?: return@onEach
                when (paymentData) {
                    is CardPaymentData -> {
                        if (paymentData.paymentUrl == paymentUrl.value) {
                            closeScreen()
                        }
                    }

                    is UrlPaymentData -> {
                        if (paymentData.paymentUrl == paymentUrl.value) {
                            closeScreen()
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun closeScreen() {
        val action = PaymentScreenAction.ScreenClosed
        emitSideEffect(SideEffect.Navigate(action))
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: PaymentScreenAction) : SideEffect
    }
}
