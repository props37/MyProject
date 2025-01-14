package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ClearCartUseCase {
    public suspend operator fun invoke(): Result<Unit>

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): ClearCartUseCase {
            return ClearCartUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
