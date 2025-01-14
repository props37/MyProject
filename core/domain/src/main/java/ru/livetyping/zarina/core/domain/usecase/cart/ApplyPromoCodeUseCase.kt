package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ApplyPromoCodeUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val promoCode: String)

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): ApplyPromoCodeUseCase {
            return ApplyPromoCodeUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
