package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface AddProductToCartUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val productId: Product.Id,
        val barcode: Barcode,
        val count: Int,
    )

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): AddProductToCartUseCase {
            return AddProductToCartUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
