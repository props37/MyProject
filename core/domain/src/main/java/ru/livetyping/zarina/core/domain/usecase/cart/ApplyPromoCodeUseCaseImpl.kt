package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.usecase.cart.ApplyPromoCodeUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ApplyPromoCodeUseCaseImpl(
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), ApplyPromoCodeUseCase {

    override suspend fun execute(params: Params) {
        cartRepository.applyPromoCode(params.promoCode)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "ApplyPromoCodeUseCaseImpl"
    }
}
