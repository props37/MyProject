package ru.livetyping.zarina.core.domain.usecase.checkout

import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.ApplyGiftCertificateUseCase.Params
import ru.livetyping.zarina.core.domain.validation.GiftCertificateValidator
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ApplyGiftCertificateUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), ApplyGiftCertificateUseCase {

    override suspend fun execute(params: Params) {
        val cert = params.giftCertificate
        validateGiftCertificate(cert)
        checkoutRepository.applyGiftCertificate(
            giftCertificate = cert,
            cartFinalPrice = params.cartFinalPrice,
            cartType = params.cartType,
        )
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private fun validateGiftCertificate(certificate: GiftCertificate) {
        val validator = GiftCertificateValidator()
        validator.validate(certificate)
    }

    private companion object {
        private const val TAG = "ApplyGiftCertificateUseCaseImpl"
    }
}
