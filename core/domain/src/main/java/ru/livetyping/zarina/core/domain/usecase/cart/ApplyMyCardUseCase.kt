package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ApplyMyCardUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val cartType: CartType,
        val productsFirstPriceSum: Int,
    )

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): ApplyMyCardUseCase {
            return ApplyMyCardUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
