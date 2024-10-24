package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import ru.livetyping.zarina.usecase.cart.ApplyBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.ApplyMyCardToCartUseCase
import ru.livetyping.zarina.usecase.cart.ApplyPromoCodeUseCase
import ru.livetyping.zarina.usecase.cart.RemoveBonusWriteOffUseCase
import ru.livetyping.zarina.usecase.cart.RemoveMyCardFromCartUseCase
import ru.livetyping.zarina.usecase.cart.RemovePromoCodeUseCase
import ru.livetyping.zarina.usecase.checkout.CheckoutUseCase
import ru.livetyping.zarina.usecase.checkout.GetCheckoutCartFlowUseCase
import ru.livetyping.zarina.usecase.checkout.GetPaymentMethodsFlowUseCase
import ru.livetyping.zarina.usecase.checkout.UpdateOrderPaymentStatusUseCase
import javax.inject.Inject

class CheckoutOrderPlacingInteractor @Inject constructor(
    val getCheckoutCartFlow: GetCheckoutCartFlowUseCase,
    val getPaymentMethodsFlow: GetPaymentMethodsFlowUseCase,
    val applyBonusWriteOff: ApplyBonusWriteOffUseCase,
    val removeBonusWriteOff: RemoveBonusWriteOffUseCase,
    val applyMyCardToCart: ApplyMyCardToCartUseCase,
    val removeMyCardFromCart: RemoveMyCardFromCartUseCase,
    val applyPromoCode: ApplyPromoCodeUseCase,
    val removePromoCode: RemovePromoCodeUseCase,
    val checkout: CheckoutUseCase,
    val updateOrderPaymentStatus: UpdateOrderPaymentStatusUseCase,
)
