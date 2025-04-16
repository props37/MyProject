package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

import ru.livetyping.zarina.core.domain.usecase.checkout.ApplyGiftCertificateUseCase
import javax.inject.Inject

internal class GiftCertificateDependencies @Inject constructor(
    val applyGiftCertificate: ApplyGiftCertificateUseCase,
)
