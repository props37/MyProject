package ru.livetyping.zarina.core.domain.usecase.product

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SubscribeToProductUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val barcode: Barcode,
        val name: String,
        val email: Email,
    )

    public companion object {
        public fun getInstance(
            productRepository: ProductRepository,
            logger: UseCaseLogger?,
        ): SubscribeToProductUseCase {
            return SubscribeToProductUseCaseImpl(
                productRepository = productRepository,
                logger = logger,
            )
        }
    }
}
