package ru.livetyping.zarina.core.domain.usecase.checkout

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ApplyGiftCertificateUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val giftCertificate: GiftCertificate,
        val cartFinalPrice: Int,
        val cartType: CartType,
    )

    public companion object {
        public fun getInstance(
            checkoutRepository: CheckoutRepository,
            logger: UseCaseLogger?,
        ): ApplyGiftCertificateUseCase {
            return ApplyGiftCertificateUseCaseImpl(
                checkoutRepository = checkoutRepository,
                logger = logger,
            )
        }
    }
}
