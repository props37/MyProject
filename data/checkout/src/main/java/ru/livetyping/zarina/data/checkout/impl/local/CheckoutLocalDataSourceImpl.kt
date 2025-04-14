package ru.livetyping.zarina.data.checkout.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PaymentData
import javax.inject.Inject

internal class CheckoutLocalDataSourceImpl @Inject constructor(
    private val completedPaymentDataHolder: CompletedPaymentDataHolder,
) : CheckoutLocalDataSource {
    override fun getCompletedPaymentsFlow(): Flow<PaymentData> {
        return completedPaymentDataHolder.getCompletedPaymentsFlow()
    }

    override fun onPaymentCompleted(paymentData: PaymentData) {
        completedPaymentDataHolder.onPaymentCompleted(paymentData)
    }

    override fun clear() {
        completedPaymentDataHolder.clear()
    }
}
