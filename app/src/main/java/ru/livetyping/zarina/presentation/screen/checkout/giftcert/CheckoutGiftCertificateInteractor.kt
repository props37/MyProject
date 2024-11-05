package ru.livetyping.zarina.presentation.screen.checkout.giftcert

import ru.livetyping.zarina.usecase.cart.ApplyGiftCertificateUseCase
import javax.inject.Inject

class CheckoutGiftCertificateInteractor @Inject constructor(
    val applyGiftCertificate: ApplyGiftCertificateUseCase,
)
