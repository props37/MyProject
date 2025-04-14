package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import ru.livetyping.zarina.core.domain.usecase.cart.ApplyMyCardUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.CancelBonusRedemptionUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.RedeemBonusesUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.WithdrawMyCardUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.WithdrawPromoCodeUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.CheckoutUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.GetCheckoutCartFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPaymentMethodsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.UpdateOrderPaymentStatusUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.WithdrawGiftCertificateUseCase
import ru.livetyping.zarina.core.domain.usecase.order.GetOrderStatusUseCase
import javax.inject.Inject

internal class OrderPlacingDependencies @Inject constructor(
    val getCheckoutCartFlow: GetCheckoutCartFlowUseCase,
    val getPaymentMethodsFlow: GetPaymentMethodsFlowUseCase,
    val redeemBonuses: RedeemBonusesUseCase,
    val cancelBonusRedemption: CancelBonusRedemptionUseCase,
    val applyMyCard: ApplyMyCardUseCase,
    val withdrawMyCard: WithdrawMyCardUseCase,
    val applyPromoCode: ApplyPromoCodeUseCase,
    val removePromoCode: WithdrawPromoCodeUseCase,
    val checkout: CheckoutUseCase,
    val updateOrderPaymentStatus: UpdateOrderPaymentStatusUseCase,
    val getOrderStatus: GetOrderStatusUseCase,
    val withdrawGiftCertificate: WithdrawGiftCertificateUseCase,
)
