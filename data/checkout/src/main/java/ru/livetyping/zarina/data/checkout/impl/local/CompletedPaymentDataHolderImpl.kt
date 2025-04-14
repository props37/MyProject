package ru.livetyping.zarina.data.checkout.impl.local

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.livetyping.zarina.core.domain.model.checkout.PaymentData
import javax.inject.Inject

internal class CompletedPaymentDataHolderImpl @Inject constructor() : CompletedPaymentDataHolder {
    private val completedPayments = MutableSharedFlow<PaymentData>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun getCompletedPaymentsFlow(): Flow<PaymentData> = completedPayments

    override fun onPaymentCompleted(paymentData: PaymentData) {
        completedPayments.tryEmit(paymentData)
    }

    override fun clear() {}
}
