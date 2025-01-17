package ru.livetyping.zarina.data.checkout.local

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.livetyping.zarina.domain.checkout.PaymentData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompletedPaymentDataHolder @Inject constructor() {
    private val completedPayments = MutableSharedFlow<PaymentData>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun getCompletedPaymentsFlow(): Flow<PaymentData> = completedPayments

    fun onPaymentCompleted(paymentData: PaymentData) {
        completedPayments.tryEmit(paymentData)
    }
}
