package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RemoveProductFromCartUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val productId: Product.Id,
        val barcode: Barcode,
    )

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): RemoveProductFromCartUseCase {
            return RemoveProductFromCartUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
