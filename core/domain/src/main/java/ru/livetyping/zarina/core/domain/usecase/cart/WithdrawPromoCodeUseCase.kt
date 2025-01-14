package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface WithdrawPromoCodeUseCase {
    public suspend operator fun invoke(): Result<Unit>

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): WithdrawPromoCodeUseCase {
            return WithdrawPromoCodeUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
