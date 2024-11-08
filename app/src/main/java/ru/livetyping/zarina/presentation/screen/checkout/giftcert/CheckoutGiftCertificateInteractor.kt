package ru.livetyping.zarina.presentation.screen.checkout.giftcert

import ru.livetyping.zarina.usecase.giftcert.ApplyGiftCertificateUseCase
import javax.inject.Inject

class CheckoutGiftCertificateInteractor @Inject constructor(
    val applyGiftCertificate: ApplyGiftCertificateUseCase,
)
