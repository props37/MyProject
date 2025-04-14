package ru.livetyping.zarina.data.checkout.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PaymentData

internal interface CompletedPaymentDataHolder {
    fun getCompletedPaymentsFlow(): Flow<PaymentData>

    fun onPaymentCompleted(paymentData: PaymentData)

    fun clear()
}
