package ru.livetyping.zarina.feature.payment.ui.impl.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.domain.usecase.checkout.GetCompletedPaymentsFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.payment.ui.api.PaymentFeature
import javax.inject.Inject

@HiltViewModel
internal class PaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCompletedPaymentsFlow: GetCompletedPaymentsFlowUseCase,
) : ViewModel(), SideEffectSource<PaymentSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<PaymentFeature.NavEntry>()

    val paymentUrl = ReadOnlyStateFlow(navEntry.paymentUrl)

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
                val isPaymentCompleted = paymentData.paymentUrl.value == paymentUrl.value
                if (isPaymentCompleted) {
                    closeScreen()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun closeScreen() {
        val action = PaymentScreenAction.BackClicked
        emitSideEffect(PaymentSideEffect.Navigate(action))
    }
}
